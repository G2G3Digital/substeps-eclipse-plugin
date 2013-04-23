/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.technophobia.substeps.model.MessageIds;
import com.technophobia.substeps.runner.junit4.JUnit4TestLoader;
//import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

/**
 * A TestRunner that reports results via a socket connection. See MessageIds for
 * more information about the protocol.
 */
public class RemoteTestRunner implements MessageSender, IVisitsTestTrees {
    /**
     * Holder for information for a rerun request
     */
    private static class RerunRequest {
        String rerunClassName;
        String rerunTestName;
        int rerunTestId;


        public RerunRequest(final int testId, final String className, final String testName) {
            this.rerunTestId = testId;
            this.rerunClassName = className;
            this.rerunTestName = testName;
        }

    }

    public enum ReranStatus {
        OK, FAILURE, ERROR;
    }

    /**
     * The name of the test classes to be executed
     */
    private String[] testClassNames;
    /**
     * The name of the test (argument -test)
     */
    private String testName;
    /**
     * The current test result
     */
    private TestExecution execution;

    /**
     * The version expected by the client
     */
    private String version = ""; //$NON-NLS-1$

    /**
     * The client socket.
     */
    private Socket clientSocket;
    /**
     * Print writer for sending messages
     */
    private PrintWriter writer;
    /**
     * Reader for incoming messages
     */
    private BufferedReader reader;
    /**
     * Host to connect to, default is the localhost
     */
    private String host = ""; //$NON-NLS-1$
    /**
     * Port to connect to.
     */
    private int port = -1;
    /**
     * Is the debug mode enabled?
     */
    private boolean debugMode = false;
    /**
     * Keep the test run server alive after a test run has finished. This allows
     * to rerun tests.
     */
    private boolean keepAlive = false;
    /**
     * Has the server been stopped
     */
    private boolean stopped = false;
    /**
     * Queue of rerun requests.
     */
    private final Vector<RerunRequest> rerunRequests = new Vector<RerunRequest>(10);
    /**
     * Thread reading from the socket
     */
    private ReaderThread readerThread;

    private String rerunTest;

    private final TestIdMap ids = new TestIdMap();

    private String[] failureNames;

    private ITestLoader loader;

    private MessageSender sender;

    private boolean consoleMode = false;

    /**
     * Reader thread that processes messages from the client.
     */
    private class ReaderThread extends Thread {
        public ReaderThread() {
            super("ReaderThread"); //$NON-NLS-1$
        }


        @Override
        public void run() {
            try {
                String message = null;
                while (true) {
                    if ((message = reader.readLine()) != null) {

                        if (message.startsWith(MessageIds.TEST_STOP)) {
                            stopped = true;
                            RemoteTestRunner.this.stop();
                            synchronized (RemoteTestRunner.this) {
                                RemoteTestRunner.this.notifyAll();
                            }
                            break;
                        }

                        else if (message.startsWith(MessageIds.TEST_RERUN)) {
                            final String arg = message.substring(MessageIds.MSG_HEADER_LENGTH);
                            // format: testId className testName
                            final int c0 = arg.indexOf(' ');
                            final int c1 = arg.indexOf(' ', c0 + 1);
                            final String s = arg.substring(0, c0);
                            final int testId = Integer.parseInt(s);
                            final String className = arg.substring(c0 + 1, c1);
                            final String name = arg.substring(c1 + 1, arg.length());
                            synchronized (RemoteTestRunner.this) {
                                rerunRequests.add(new RerunRequest(testId, className, name));
                                RemoteTestRunner.this.notifyAll();
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                RemoteTestRunner.this.stop();
            }
        }
    }


    public RemoteTestRunner() {
        setMessageSender(this);
    }


    public void setMessageSender(final MessageSender sender) {
        this.sender = sender;
    }


    /**
     * The main entry point.
     * 
     * @param args
     *            Parameters:
     * 
     *            <pre>
     * -classnames: the name of the test suite class
     * -testfilename: the name of a file containing classnames of test suites
     * -test: the test method name (format classname testname)
     * -host: the host to connect to default local host
     * -port: the port to connect to, mandatory argument
     * -keepalive: keep the process alive after a test run
     * </pre>
     */
    public static void main(final String[] args) {
        try {
            final RemoteTestRunner testRunServer = new RemoteTestRunner();
            testRunServer.init(args);
            testRunServer.run();
        } catch (final Throwable e) {
            e.printStackTrace(); // don't allow System.exit(0) to swallow
                                 // exceptions
        } finally {
            // fix for 14434
            System.exit(0);
        }
    }


    /**
     * Parse command line arguments. Hook for subclasses to process additional
     * arguments.
     * 
     * @param args
     *            the arguments
     */
    protected void init(final String[] args) {
        defaultInit(args);
    }


    /**
     * The class loader to be used for loading tests. Subclasses may override to
     * use another class loader.
     * 
     * @return the class loader to lead test classes
     */
    protected ClassLoader getTestClassLoader() {
        return getClass().getClassLoader();
    }


    /**
     * Process the default arguments.
     * 
     * @param args
     *            arguments
     */
    protected final void defaultInit(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().startsWith("classnames") || args[i].toLowerCase().equals("classname")) { //$NON-NLS-1$ //$NON-NLS-2$
                final List<String> list = valueAsList(args[i]);
                testClassNames = list.toArray(new String[list.size()]);
            } else if (args[i].toLowerCase().startsWith("test")) { //$NON-NLS-1$
                String name = valueAsString(args[i]);
                final int p = testName.indexOf(':');
                if (p == -1)
                    throw new IllegalArgumentException("Testname not separated by \'%\'"); //$NON-NLS-1$
                name = name.substring(p + 1);
                testClassNames = new String[] { name.substring(0, p) };
            } else if (args[i].toLowerCase().startsWith("testnamefile")) { //$NON-NLS-1$
                final String testNameFile = valueAsString(args[i]);
                try {
                    readTestNames(testNameFile);
                } catch (final IOException e) {
                    throw new IllegalArgumentException("Cannot read testname file."); //$NON-NLS-1$
                }

            } else if (args[i].toLowerCase().startsWith("testfailures")) { //$NON-NLS-1$
                final String testFailuresFile = valueAsString(args[i]);
                try {
                    readFailureNames(testFailuresFile);
                } catch (final IOException e) {
                    throw new IllegalArgumentException("Cannot read testfailures file."); //$NON-NLS-1$
                }
            } else if (args[i].toLowerCase().startsWith("port")) { //$NON-NLS-1$
                port = Integer.parseInt(valueAsString(args[i]));
            } else if (args[i].toLowerCase().startsWith("host")) { //$NON-NLS-1$
                host = valueAsString(args[i]);
            } else if (args[i].toLowerCase().startsWith("rerun")) { //$NON-NLS-1$
                rerunTest = valueAsString(args[i]);
            } else if (args[i].toLowerCase().startsWith("keepalive")) { //$NON-NLS-1$
                keepAlive = true;
            } else if (args[i].toLowerCase().startsWith("debugging") || args[i].toLowerCase().startsWith("debug")) { //$NON-NLS-1$ //$NON-NLS-2$
                debugMode = true;
            } else if (args[i].toLowerCase().startsWith("version")) { //$NON-NLS-1$
                version = valueAsString(args[i]);
            } else if (args[i].toLowerCase().startsWith("junitconsole")) { //$NON-NLS-1$
                consoleMode = true;
            } else if (args[i].toLowerCase().startsWith("testloaderclass")) { //$NON-NLS-1$
                final String className = valueAsString(args[i]);
                createLoader(className);
                i++;
            }
        }

        if (getTestLoader() == null)
            initDefaultLoader();

        if (testClassNames == null || testClassNames.length == 0)
            throw new IllegalArgumentException("Error: parameter '-classNames' or '-className' not specified");

        if (port == -1)
            throw new IllegalArgumentException("Error: parameter '-port' not specified");
        if (debugMode)
            System.out.println("keepalive " + keepAlive); //$NON-NLS-1$
    }


    public void initDefaultLoader() {
        createLoader(JUnit4TestLoader.class.getName());
    }


    public void createLoader(final String className) {
        setLoader(createRawTestLoader(className));
    }


    protected ITestLoader createRawTestLoader(final String className) {
        try {
            return (ITestLoader) loadTestLoaderClass(className).newInstance();
        } catch (final Exception e) {
            final StringWriter trace = new StringWriter();
            e.printStackTrace(new PrintWriter(trace));
            final String message = "Error: test loader "+className+" not found:\n"+trace.toString();
                    
            throw new IllegalArgumentException(message);
        }
    }


    protected Class<?> loadTestLoaderClass(final String className) throws ClassNotFoundException {
        return Class.forName(className);
    }


    public void setLoader(final ITestLoader newInstance) {
        loader = newInstance;
    }


    private void readTestNames(final String testNameFile) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(testNameFile)),
                "UTF-8")); //$NON-NLS-1$
        try {
            String line;
            final Vector<String> list = new Vector<String>();
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            testClassNames = list.toArray(new String[list.size()]);
        } finally {
            br.close();
        }
        if (debugMode) {
            System.out.println("Tests:"); //$NON-NLS-1$
            for (int i = 0; i < testClassNames.length; i++) {
                System.out.println("    " + testClassNames[i]); //$NON-NLS-1$
            }
        }
    }


    private void readFailureNames(final String testFailureFile) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File(testFailureFile)), "UTF-8")); //$NON-NLS-1$
        try {
            String line;
            final Vector<String> list = new Vector<String>();
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            failureNames = list.toArray(new String[list.size()]);
        } finally {
            br.close();
        }
        if (debugMode) {
            System.out.println("Failures:"); //$NON-NLS-1$
            for (int i = 0; i < failureNames.length; i++) {
                System.out.println("    " + failureNames[i]); //$NON-NLS-1$
            }
        }
    }


    /**
     * Connects to the remote ports and runs the tests.
     */
    protected void run() {
        if (!connect())
            return;
        if (rerunTest != null) {
            rerunTest(new RerunRequest(Integer.parseInt(rerunTest), testClassNames[0], testName));
            return;
        }

        final FirstRunExecutionListener listener = firstRunExecutionListener();
        execution = new TestExecution(listener, getClassifier());
        runTests(execution);
        if (keepAlive)
            waitForReruns();

        shutDown();

    }


    public FirstRunExecutionListener firstRunExecutionListener() {
        return new FirstRunExecutionListener(sender, ids);
    }


    /**
     * Waits for rerun requests until an explicit stop request
     */
    private synchronized void waitForReruns() {
        while (!stopped) {
            try {
                wait();
                if (!stopped && rerunRequests.size() > 0) {
                    final RerunRequest r = rerunRequests.remove(0);
                    rerunTest(r);
                }
            } catch (final InterruptedException e) {
                // No-op
            }
        }
    }


    public void runFailed(final String message, final Exception exception) {
        // TODO: remove System.err.println?
        System.err.println(message);
        if (exception != null)
            exception.printStackTrace(System.err);
    }


    protected Class<?>[] loadClasses(final String[] classNames) {
        final Vector<Class<?>> classes = new Vector<Class<?>>();
        for (int i = 0; i < classNames.length; i++) {
            final String name = classNames[i];
            final Class<?> clazz = loadClass(name, this);
            if (clazz != null) {
                classes.add(clazz);
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }


    protected void notifyListenersOfTestEnd(final TestExecution testExecution, final long testStartTime) {
        if (testExecution == null || testExecution.shouldStop())
            notifyTestRunStopped(System.currentTimeMillis() - testStartTime);
        else
            notifyTestRunEnded(System.currentTimeMillis() - testStartTime);
    }


    /**
     * Runs a set of tests.
     * 
     * @param testClassNames
     *            classes to be run
     * @param testName
     *            individual method to be run
     * @param execution
     *            executor
     */
    public void runTests(final String[] classNames, final String name, final TestExecution testExecution) {
        final ITestReference[] suites = loader.loadTests(loadClasses(classNames), name, failureNames, this);

        // count all testMethods and inform ITestRunListeners
        final int count = countTests(suites);

        notifyTestRunStarted(count);

        if (count == 0) {
            notifyTestRunEnded(0);
            return;
        }

        sendTrees(suites);

        final long testStartTime = System.currentTimeMillis();
        testExecution.run(suites);
        notifyListenersOfTestEnd(testExecution, testStartTime);
    }


    private void sendTrees(final ITestReference[] suites) {
        final long startTime = System.currentTimeMillis();
        if (debugMode)
            System.out.print("start send tree..."); //$NON-NLS-1$
        for (int i = 0; i < suites.length; i++) {
            suites[i].sendTree(this);
        }
        if (debugMode)
            System.out.println("done send tree - time(ms): " + (System.currentTimeMillis() - startTime)); //$NON-NLS-1$
    }


    private int countTests(final ITestReference[] tests) {
        int count = 0;
        for (int i = 0; i < tests.length; i++) {
            final ITestReference test = tests[i];
            if (test != null)
                count = count + test.countTestCases();
        }
        return count;
    }


    /**
     * Reruns a test as defined by the fully qualified class name and the name
     * of the test.
     * 
     * @param r
     *            rerun request
     */
    public void rerunTest(final RerunRequest r) {
        final Class<?>[] classes = loadClasses(new String[] { r.rerunClassName });
        final ITestReference rerunTest1 = loader.loadTests(classes, r.rerunTestName, null, this)[0];
        final RerunExecutionListener service = rerunExecutionListener();

        final TestExecution testExecution = new TestExecution(service, getClassifier());
        final ITestReference[] suites = new ITestReference[] { rerunTest1 };
        testExecution.run(suites);

        notifyRerunComplete(r, service.getStatus());
    }


    public RerunExecutionListener rerunExecutionListener() {
        return new RerunExecutionListener(sender, ids);
    }


    protected IClassifiesThrowables getClassifier() {
        return new DefaultClassifier(version);
    }


    @Override
    public void visitTreeEntry(final ITestIdentifier id, final boolean b, final int i) {
        notifyTestTreeEntry(getTestId(id) + ',' + escapeComma(id.getName()) + ',' + b + ',' + i);
    }


    private String escapeComma(final String s) {
        if ((s.indexOf(',') < 0) && (s.indexOf('\\') < 0))
            return s;
        final StringBuffer sb = new StringBuffer(s.length() + 10);
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c == ',')
                sb.append("\\,"); //$NON-NLS-1$
            else if (c == '\\')
                sb.append("\\\\"); //$NON-NLS-1$
            else
                sb.append(c);
        }
        return sb.toString();
    }


    // WANT: work in bug fixes since RC2?
    private String getTestId(final ITestIdentifier id) {
        return ids.getTestId(id);
    }


    /**
     * Stop the current test run.
     */
    protected void stop() {
        if (execution != null) {
            execution.stop();
        }
    }


    /**
     * Connect to the remote test listener.
     * 
     * @return <code>true</code> if connection successful, <code>false</code> if
     *         failed
     */
    protected boolean connect() {
        if (consoleMode) {
            clientSocket = null;
            writer = new PrintWriter(System.out);
            reader = new BufferedReader(new InputStreamReader(System.in));
            readerThread = new ReaderThread();
            readerThread.start();
            return true;
        }
        if (debugMode)
            System.out.println("RemoteTestRunner: trying to connect" + host + ":" + port);
        Exception exception = null;
        for (int i = 1; i < 20; i++) {
            try {
                clientSocket = new Socket(host, port);
                try {
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
                            "UTF-8")), false/* true */); //$NON-NLS-1$
                } catch (final UnsupportedEncodingException e1) {
                    writer = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), false/* true */);
                }
                try {
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8")); //$NON-NLS-1$
                } catch (final UnsupportedEncodingException e1) {
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                }
                readerThread = new ReaderThread();
                readerThread.start();
                return true;
            } catch (final IOException e) {
                exception = e;
            }
            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e) {
                // No-op
            }
        }

        runFailed("Could not connect to: "+host+" : "+port
                        , exception);
        return false;
    }


    /**
     * Shutsdown the connection to the remote test listener.
     */
    private void shutDown() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
        try {
            if (readerThread != null) {
                // interrupt reader thread so that we don't block on close
                // on a lock held by the BufferedReader
                // fix for bug: 38955
                readerThread.interrupt();
            }
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (final IOException e) {
            if (debugMode)
                e.printStackTrace();
        }

        try {
            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }
        } catch (final IOException e) {
            if (debugMode)
                e.printStackTrace();
        }
    }


    /*
     * @see
     * org.eclipse.jdt.internal.junit.runner.MessageSender#sendMessage(java.
     * lang.String)
     */
    @Override
    public void sendMessage(final String msg) {
        if (writer == null)
            return;
        writer.println(msg);
        // if (!fConsoleMode)
        // System.out.println(msg);
    }


    protected void notifyTestRunStarted(final int testCount) {
        sender.sendMessage(MessageIds.TEST_RUN_START + testCount + " " + "v2"); //$NON-NLS-1$ //$NON-NLS-2$
    }


    private void notifyTestRunEnded(final long elapsedTime) {
        sender.sendMessage(MessageIds.TEST_RUN_END + elapsedTime);
        sender.flush();
        // shutDown();
    }


    protected void notifyTestRunStopped(final long elapsedTime) {
        sender.sendMessage(MessageIds.TEST_STOPPED + elapsedTime);
        sender.flush();
        // shutDown();
    }


    protected void notifyTestTreeEntry(final String treeEntry) {
        sender.sendMessage(MessageIds.TEST_TREE + treeEntry);
    }


    /*
     * @see org.eclipse.jdt.internal.junit.runner.RerunCompletionListener#
     * notifyRerunComplete(org.eclipse.jdt.internal.junit.runner.RerunRequest,
     * java.lang.String)
     */
    public void notifyRerunComplete(final RerunRequest r, final ReranStatus status) {
        if (port != -1) {
            sender.sendMessage(MessageIds.TEST_RERAN + r.rerunTestId
                    + " " + r.rerunClassName + " " + r.rerunTestName + " " + status); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sender.flush();
        }
    }


    @Override
    public void flush() {
        writer.flush();
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.junit.runner.TestRunner#runTests(org.eclipse
     * .jdt.internal.junit.runner.RemoteTestRunner.TestExecution)
     */
    public void runTests(final TestExecution testExecution) {
        runTests(testClassNames, testName, testExecution);
    }


    public ITestLoader getTestLoader() {
        return loader;
    }


    public Class<?> loadClass(final String className, final RemoteTestRunner listener) {
        Class<?> clazz = null;
        try {
            clazz = getTestClassLoader().loadClass(className);
        } catch (final ClassNotFoundException e) {
            listener.runFailed("Class not found "+className, e);
        }
        return clazz;
    }
    
    private List<String> valueAsList(String arg){
    	String[] kv = arg.split("=");
    	if(kv.length != 2){
    		// No idea what to do - should be key=value
    		return null;
    	}
    	String[] listItems = kv[1].split(",");
    	return Arrays.asList(listItems);
    }
    
    private String valueAsString(String arg){
    	String[] kv = arg.split("=");
    	if(kv.length != 2){
    		// No idea what to do - should be key=value
    		return null;
    	}
    	return kv[1];
    }
}
