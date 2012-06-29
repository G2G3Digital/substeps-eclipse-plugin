package com.technophobia.substeps.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.junit.ui.SubstepsRunSessionImpl;
import com.technophobia.substeps.junit.ui.SubstepsRunSessionListener;
import com.technophobia.substeps.model.structure.DefaultSubstepsTestElementFactory;
import com.technophobia.substeps.model.structure.SubstepsTestElementFactory;
import com.technophobia.substeps.preferences.PreferencesConstants;

public class SubstepsModel {

    private final class SubstepsLaunchListener implements ILaunchListener {

        /**
         * Used to track new launches. We need to do this so that we only attach
         * a TestRunner once to a launch. Once a test runner is connected, it is
         * removed from the set.
         */
        private final HashSet<ILaunch> trackedLaunches = new HashSet<ILaunch>(20);


        /*
         * @see ILaunchListener#launchAdded(ILaunch)
         */
        @Override
        public void launchAdded(final ILaunch launch) {
            trackedLaunches.add(launch);
        }


        /*
         * @see ILaunchListener#launchRemoved(ILaunch)
         */
        @Override
        public void launchRemoved(final ILaunch launch) {
            trackedLaunches.remove(launch);
            // TODO: story for removing old test runs?
            // getDisplay().asyncExec(new Runnable() {
            // public void run() {
            // TestRunnerViewPart testRunnerViewPart=
            // findTestRunnerViewPartInActivePage();
            // if (testRunnerViewPart != null && testRunnerViewPart.isCreated()
            // && launch.equals(testRunnerViewPart.getLastLaunch()))
            // testRunnerViewPart.reset();
            // }
            // });
        }


        /*
         * @see ILaunchListener#launchChanged(ILaunch)
         */
        @Override
        public void launchChanged(final ILaunch launch) {
            if (!trackedLaunches.contains(launch))
                return;

            final ILaunchConfiguration config = launch.getLaunchConfiguration();
            if (config == null)
                return;

            final IJavaProject javaProject = SubstepsLaunchConfigurationConstants.getJavaProject(config);
            if (javaProject == null)
                return;

            // test whether the launch defines the JUnit attributes
            final String portStr = launch.getAttribute(SubstepsLaunchConfigurationConstants.ATTR_PORT);
            if (portStr == null)
                return;
            try {
                final int port = Integer.parseInt(portStr);
                trackedLaunches.remove(launch);
                connectTestRunner(launch, javaProject, port);
            } catch (final NumberFormatException e) {
                return;
            }
        }


        private void connectTestRunner(final ILaunch launch, final IJavaProject javaProject, final int port) {
            final SubstepsRunSession substepsRunSession = new SubstepsRunSessionImpl(launch, testElementFactory,
                    javaProject, port);
            addTestRunSession(substepsRunSession);

            final Object[] listeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners().getListeners();
            for (int i = 0; i < listeners.length; i++) {
                ((SubstepsRunListener) listeners[i]).sessionLaunched(substepsRunSession);
            }
        }
    }

    private final ListenerList testRunSessionListeners = new ListenerList();

    /**
     * Active test run sessions, youngest first.
     */
    private final LinkedList<SubstepsRunSession> substepsRunSessions = new LinkedList<SubstepsRunSession>();
    private final ILaunchListener launchListener = new SubstepsLaunchListener();

    // for run session
    private final SubstepsTestElementFactory testElementFactory = new DefaultSubstepsTestElementFactory();


    /**
     * Starts the model (called by the {@link FeatureRunnerPlugin} on startup).
     */
    public void start() {
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchManager.addLaunchListener(launchListener);

        addTestRunSessionListener(new SubstepsRunSessionListenerImpl());
    }


    /**
     * Stops the model (called by the {@link FeatureRunnerPlugin} on shutdown).
     */
    public void stop() {
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchManager.removeLaunchListener(launchListener);

        final File historyDirectory = FeatureRunnerPlugin.instance().getHistoryDirectory();
        final File[] swapFiles = historyDirectory.listFiles();
        if (swapFiles != null) {
            for (int i = 0; i < swapFiles.length; i++) {
                swapFiles[i].delete();
            }
        }

        // for (Iterator iter= fTestRunSessions.iterator(); iter.hasNext();) {
        // final TestRunSession session= (TestRunSession) iter.next();
        // SafeRunner.run(new ISafeRunnable() {
        // public void run() throws Exception {
        // session.swapOut();
        // }
        // public void handleException(Throwable exception) {
        // JUnitPlugin.log(exception);
        // }
        // });
        // }
    }


    public void addTestRunSessionListener(final SubstepsRunSessionListener listener) {
        testRunSessionListeners.add(listener);
    }


    public void removeTestRunSessionListener(final SubstepsRunSessionListener listener) {
        testRunSessionListeners.remove(listener);
    }


    /**
     * @return a list of active {@link SubstepsRunSession}s. The list is a copy
     *         of the internal data structure and modifications do not affect
     *         the global list of active sessions. The list is sorted by age,
     *         youngest first.
     */
    public synchronized List<SubstepsRunSession> getTestRunSessions() {
        return new ArrayList<SubstepsRunSession>(substepsRunSessions);
    }


    /**
     * Adds the given {@link SubstepsRunSession} and notifies all registered
     * {@link SubstepsRunSessionListener}s.
     * 
     * @param substepsRunSession
     *            the session to add
     */
    public void addTestRunSession(final SubstepsRunSession substepsRunSession) {
        Assert.isNotNull(substepsRunSession);
        final ArrayList<SubstepsRunSession> toRemove = new ArrayList<SubstepsRunSession>();

        synchronized (this) {
            Assert.isLegal(!substepsRunSessions.contains(substepsRunSession));
            substepsRunSessions.addFirst(substepsRunSession);

            final int maxCount = Platform.getPreferencesService().getInt(FeatureRunnerPlugin.PLUGIN_ID,
                    PreferencesConstants.MAX_TEST_RUNS, 10, null);
            final int size = substepsRunSessions.size();
            if (size > maxCount) {
                final List<SubstepsRunSession> excess = substepsRunSessions.subList(maxCount, size);
                for (final Iterator<SubstepsRunSession> iter = excess.iterator(); iter.hasNext();) {
                    final SubstepsRunSession oldSession = iter.next();
                    if (!(oldSession.isStarting() || oldSession.isRunning() || oldSession.isKeptAlive())) {
                        toRemove.add(oldSession);
                        iter.remove();
                    }
                }
            }
        }

        for (int i = 0; i < toRemove.size(); i++) {
            final SubstepsRunSession oldSession = toRemove.get(i);
            notifyTestRunSessionRemoved(oldSession);
        }
        notifyTestRunSessionAdded(substepsRunSession);
    }


    /**
     * Imports a test run session from the given file.
     * 
     * @param file
     *            a file containing a test run session transcript
     * @return the imported test run session
     * @throws CoreException
     *             if the import failed
     */
    /*
     * public static SubstepsRunSession importTestRunSession(final File file)
     * throws CoreException { try { final SAXParserFactory parserFactory =
     * SAXParserFactory.newInstance(); // parserFactory.setValidating(true); //
     * TODO: add DTD and debug // flag final SAXParser parser =
     * parserFactory.newSAXParser(); final SubstepsRunHandler handler = new
     * SubstepsRunHandler(); parser.parse(file, handler); final
     * SubstepsRunSession session = handler.getTestRunSession();
     * JUnitCorePlugin.getModel().addTestRunSession(session); return session; }
     * catch (final ParserConfigurationException e) { throwImportError(file, e);
     * } catch (final SAXException e) { throwImportError(file, e); } catch
     * (final IOException e) { throwImportError(file, e); } catch (final
     * IllegalArgumentException e) { // Bug in parser: can throw IAE even if
     * file is not null throwImportError(file, e); } return null; // does not
     * happen }
     */

    /**
     * Imports a test run session from the given URL.
     * 
     * @param url
     *            an URL to a test run session transcript
     * @param monitor
     *            a progress monitor for cancellation
     * @return the imported test run session
     * @throws InvocationTargetException
     *             wrapping a CoreException if the import failed
     * @throws InterruptedException
     *             if the import was cancelled
     * @since 3.6
     */
    /*
     * public static SubstepsRunSession importTestRunSession(final String url,
     * final IProgressMonitor monitor) throws InvocationTargetException,
     * InterruptedException {
     * monitor.beginTask(SubstepsFeatureMessages.Model_importing_from_url,
     * IProgressMonitor.UNKNOWN); final String trimmedUrl =
     * url.trim().replaceAll("\r\n?|\n", ""); //$NON-NLS-1$ //$NON-NLS-2$ final
     * SubstepsRunHandler handler = new SubstepsRunHandler(monitor);
     * 
     * final CoreException[] exception = { null }; final SubstepsRunSession[]
     * session = { null };
     * 
     * final Thread importThread = new Thread("Substeps URL importer") {
     * //$NON-NLS-1$
     * 
     * @Override public void run() { try { final SAXParserFactory parserFactory
     * = SAXParserFactory.newInstance(); // parserFactory.setValidating(true);
     * // TODO: add DTD and // debug flag final SAXParser parser =
     * parserFactory.newSAXParser(); parser.parse(trimmedUrl, handler);
     * session[0] = handler.getTestRunSession(); } catch (final
     * OperationCanceledException e) { // canceled } catch (final
     * ParserConfigurationException e) { storeImportError(e); } catch (final
     * SAXException e) { storeImportError(e); } catch (final IOException e) {
     * storeImportError(e); } catch (final IllegalArgumentException e) { // Bug
     * in parser: can throw IAE even if URL is not null storeImportError(e); } }
     * 
     * 
     * private void storeImportError(final Exception e) { exception[0] = new
     * CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR,
     * FeatureRunnerPlugin.PLUGIN_ID,
     * SubstepsFeatureMessages.Model_could_not_import, e)); } };
     * importThread.start();
     * 
     * while (session[0] == null && exception[0] == null &&
     * !monitor.isCanceled()) { try { Thread.sleep(100); } catch (final
     * InterruptedException e) { // that's OK } } if (session[0] == null) { if
     * (exception[0] != null) { throw new
     * InvocationTargetException(exception[0]); } else {
     * importThread.interrupt(); // have to kill the thread since we // don't
     * control URLConnection and XML // parsing throw new
     * InterruptedException(); } }
     * 
     * JUnitCorePlugin.getModel().addTestRunSession(session[0]); monitor.done();
     * return session[0]; }
     * 
     * 
     * public static void importIntoTestRunSession(final File swapFile, final
     * SubstepsRunSession testRunSession) throws CoreException { try { final
     * SAXParserFactory parserFactory = SAXParserFactory.newInstance(); //
     * parserFactory.setValidating(true); // TODO: add DTD and debug // flag
     * final SAXParser parser = parserFactory.newSAXParser(); final
     * SubstepsRunHandler handler = new SubstepsRunHandler(testRunSession);
     * parser.parse(swapFile, handler); } catch (final
     * ParserConfigurationException e) { throwImportError(swapFile, e); } catch
     * (final SAXException e) { throwImportError(swapFile, e); } catch (final
     * IOException e) { throwImportError(swapFile, e); } catch (final
     * IllegalArgumentException e) { // Bug in parser: can throw IAE even if
     * file is not null throwImportError(swapFile, e); } }
     */

    /**
     * Exports the given test run session.
     * 
     * @param testRunSession
     *            the test run session
     * @param file
     *            the destination
     * @throws CoreException
     *             if an error occurred
     */
    /*
     * public static void exportTestRunSession(final SubstepsRunSession
     * testRunSession, final File file) throws CoreException { FileOutputStream
     * out = null; try { out = new FileOutputStream(file);
     * exportTestRunSession(testRunSession, out);
     * 
     * } catch (final IOException e) { throwExportError(file, e); } catch (final
     * TransformerConfigurationException e) { throwExportError(file, e); } catch
     * (final TransformerException e) { throwExportError(file, e); } finally {
     * if (out != null) { try { out.close(); } catch (final IOException e2) {
     * FeatureRunnerPlugin.log(Status.ERROR, e2); } } } }
     * 
     * 
     * public static void exportTestRunSession(final SubstepsRunSession
     * testRunSession, final OutputStream out) throws
     * TransformerFactoryConfigurationError, TransformerException {
     * 
     * final Transformer transformer =
     * TransformerFactory.newInstance().newTransformer(); final InputSource
     * inputSource = new InputSource(); final SAXSource source = new
     * SAXSource(new SubstepsRunSessionSerializer(testRunSession), inputSource);
     * final StreamResult result = new StreamResult(out);
     * transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
     * //$NON-NLS-1$ transformer.setOutputProperty(OutputKeys.INDENT, "yes");
     * //$NON-NLS-1$
     * 
     * Bug in Xalan: Only indents if proprietary property
     * org.apache.xalan.templates.OutputProperties.S_KEY_INDENT_AMOUNT is set.
     * 
     * Bug in Xalan as shipped with J2SE 5.0: Does not read the indent-amount
     * property at all >:-(.
     * 
     * try {
     * transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount"
     * , "2"); //$NON-NLS-1$ //$NON-NLS-2$ } catch (final
     * IllegalArgumentException e) { // no indentation today... }
     * transformer.transform(source, result); }
     */

    /*
     * private static void throwExportError(final File file, final Exception e)
     * throws CoreException { throw new CoreException(new
     * org.eclipse.core.runtime.Status(IStatus.ERROR,
     * JUnitCorePlugin.getPluginId(),
     * MessageFormat.format(SubstepsFeatureMessages.Model_could_not_write,
     * BasicElementLabels.getPathLabel(file)), e)); }
     * 
     * 
     * private static void throwImportError(final File file, final Exception e)
     * throws CoreException { throw new CoreException(new
     * org.eclipse.core.runtime.Status(IStatus.ERROR,
     * JUnitCorePlugin.getPluginId(),
     * MessageFormat.format(SubstepsFeatureMessages.Model_could_not_read,
     * BasicElementLabels.getPathLabel(file)), e)); }
     */

    /**
     * Removes the given {@link SubstepsRunSession} and notifies all registered
     * {@link SubstepsRunSessionListener}s.
     * 
     * @param testRunSession
     *            the session to remove
     */
    public void removeTestRunSession(final SubstepsRunSession testRunSession) {
        boolean existed;
        synchronized (this) {
            existed = substepsRunSessions.remove(testRunSession);
        }
        if (existed) {
            notifyTestRunSessionRemoved(testRunSession);
        }
        testRunSession.removeSwapFile();
    }


    private void notifyTestRunSessionRemoved(final SubstepsRunSession testRunSession) {
        testRunSession.stopTestRun();
        final ILaunch launch = testRunSession.getLaunch();
        if (launch != null) {
            final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            launchManager.removeLaunch(launch);
        }

        final Object[] listeners = testRunSessionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((SubstepsRunSessionListener) listeners[i]).sessionRemoved(testRunSession);
        }
    }


    private void notifyTestRunSessionAdded(final SubstepsRunSession testRunSession) {
        final Object[] listeners = testRunSessionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((SubstepsRunSessionListener) listeners[i]).sessionAdded(testRunSession);
        }
    }


    public SubstepsRunListener[] getTestRunListeners() {
        // TODO Auto-generated method stub
        return null;
    }

}
