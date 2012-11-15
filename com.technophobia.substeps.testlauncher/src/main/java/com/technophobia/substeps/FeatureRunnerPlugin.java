package com.technophobia.substeps;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import com.technophobia.substeps.junit.ui.SubstepsFeatureTestRunnerViewPart;
import com.technophobia.substeps.model.SubstepsModel;
import com.technophobia.substeps.model.SubstepsRunListener;

public class FeatureRunnerPlugin extends AbstractUIPlugin implements BundleActivator {

    public static final String PLUGIN_ID = "com.technophobia.substeps.testlauncher";
    private static final String HISTORY_DIR_NAME = "history";
    private static final String ID_EXTENSION_POINT_SUBSTEPS_RUN_LISTENERS = PLUGIN_ID + ".substepsRunListeners";

    private static FeatureRunnerPlugin pluginInstance;

    private ILog log;

    private final SubstepsModel model;

    private boolean isStarted;

    private ListenerList listeners = null;


    public FeatureRunnerPlugin() {
        super();
        this.isStarted = false;
        FeatureRunnerPlugin.pluginInstance = this;
        this.model = new SubstepsModel();
    }


    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
        log = Platform.getLog(bundleContext.getBundle());
        model.start();
        this.isStarted = true;
    }


    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        this.isStarted = false;
        log = null;
        try {
            InstanceScope.INSTANCE.getNode(FeatureRunnerPlugin.PLUGIN_ID).flush();
            model.stop();
        } finally {
            super.stop(bundleContext);
        }
    }


    public Bundle getBundle(final String bundleName) {
        final Bundle[] bundles = getBundles(bundleName, null);
        if (bundles != null && bundles.length > 0)
            return bundles[0];
        return null;
    }


    @SuppressWarnings("deprecation")
    public Bundle[] getBundles(final String bundleName, final String version) {
        Bundle[] bundles = Platform.getBundles(bundleName, version);
        if (bundles != null)
            return bundles;

        // Accessing unresolved bundle
        final BundleContext bundleContext = getBundle().getBundleContext();
        final ServiceReference<?> serviceRef = bundleContext.getServiceReference(PackageAdmin.class.getName());
        final PackageAdmin admin = (PackageAdmin) bundleContext.getService(serviceRef);
        bundles = admin.getBundles(bundleName, version);
        if (bundles != null && bundles.length > 0)
            return bundles;
        return null;
    }


    public boolean isStopped() {
        return !isStarted;
    }


    public Shell getActiveShell() {
        return getActiveWorkbenchWindow().getShell();
    }


    public IWorkbenchWindow getActiveWorkbenchWindow() {
        final IWorkbench workBench = getWorkbench();
        if (workBench == null)
            return null;
        if (workBench.getActiveWorkbenchWindow() != null) {
            return workBench.getActiveWorkbenchWindow();
        } else if (workBench.getWorkbenchWindowCount() > 0) {
            log(IStatus.WARNING,
                    "Could not find any active workbench window, returning window 1 of "
                            + workBench.getWorkbenchWindowCount());
            return workBench.getWorkbenchWindows()[0];
        }
        log(IStatus.WARNING, "Could not find workbench window, returning null");
        return null;
    }


    public static void log(final int status, final String message) {
        instance().log.log(new Status(status, PLUGIN_ID, message));
    }


    public static void log(final Throwable ex) {
        instance().log.log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, "Error", ex));
    }


    public static FeatureRunnerPlugin instance() {
        return pluginInstance;
    }


    public ListenerList getSubstepsRunListeners() {
        if (listeners == null) {
            listeners = loadFromRegistry();
        }
        return listeners;
    }


    private ListenerList loadFromRegistry() {
        final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                ID_EXTENSION_POINT_SUBSTEPS_RUN_LISTENERS);
        if (extensionPoint == null) {
            return null;
        }
        final IConfigurationElement[] configs = extensionPoint.getConfigurationElements();
        final ListenerList loadedListeners = new ListenerList();
        for (int i = 0; i < configs.length; i++) {
            try {
                final Object substepsRunListener = configs[i].createExecutableExtension("class"); //$NON-NLS-1$
                if (substepsRunListener instanceof SubstepsRunListener) {
                    loadedListeners.add(substepsRunListener);
                }
            } catch (final CoreException e) {
                log(e);
            }
        }
        return loadedListeners;
    }


    public File getHistoryDirectory() throws IllegalStateException {
        final File historyDir = getStateLocation().append(HISTORY_DIR_NAME).toFile();
        if (!historyDir.isDirectory()) {
            historyDir.mkdir();
        }
        return historyDir;
    }


    public SubstepsModel getModel() {
        return model;
    }


    public void asyncShowSubstepsRunnerViewPart() {
        getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                showTestRunnerViewPartInActivePage();
            }
        });
    }


    private Display getDisplay() {
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        return display;
    }


    public SubstepsFeatureTestRunnerViewPart showTestRunnerViewPartInActivePage() {
        try {
            // Have to force the creation of view part contents
            // otherwise the UI will not be updated
            final IWorkbenchWindow window = getActiveWorkbenchWindow();
            final IWorkbenchPage page = window != null ? window.getActivePage() : null;
            if (page == null)
                return null;
            final SubstepsFeatureTestRunnerViewPart view = (SubstepsFeatureTestRunnerViewPart) page
                    .findView(SubstepsFeatureTestRunnerViewPart.NAME);
            if (view == null) {
                // create and show the result view if it isn't created yet.
                return (SubstepsFeatureTestRunnerViewPart) page.showView(SubstepsFeatureTestRunnerViewPart.NAME, null,
                        IWorkbenchPage.VIEW_VISIBLE);
            }
            return view;
        } catch (final PartInitException pie) {
            log(pie);
            return null;
        }
    }
}
