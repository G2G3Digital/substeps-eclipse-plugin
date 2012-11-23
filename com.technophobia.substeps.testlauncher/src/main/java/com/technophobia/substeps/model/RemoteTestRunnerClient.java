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
package com.technophobia.substeps.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.model.MessageIds.MessageStatus;

/**
 * The client side of the RemoteTestRunner. Handles the marshaling of the
 * different messages.
 */
public class RemoteTestRunnerClient {

    public abstract class ListenerSafeRunnable implements ISafeRunnable {
        @Override
        public void handleException(final Throwable exception) {
            FeatureRunnerPlugin.log(exception);
        }
    }

    /**
     * A simple state machine to process requests from the RemoteTestRunner
     */
    abstract class ProcessingState {
        abstract ProcessingState readMessage(String message);
    }

    class DefaultProcessingState extends ProcessingState {
        @Override
        ProcessingState readMessage(final String message) {
            if (message.startsWith(MessageIds.TRACE_START)) {
                failedTrace.setLength(0);
                return traceState;
            }
            if (message.startsWith(MessageIds.EXPECTED_START)) {
                expectedResult.setLength(0);
                return expectedState;
            }
            if (message.startsWith(MessageIds.ACTUAL_START)) {
                actualResult.setLength(0);
                return actualState;
            }
            if (message.startsWith(MessageIds.RTRACE_START)) {
                failedRerunTrace.setLength(0);
                return rerunState;
            }
            final String arg = message.substring(MessageIds.MSG_HEADER_LENGTH);
            if (message.startsWith(MessageIds.TEST_RUN_START)) {
                // version < 2 format: count
                // version >= 2 format: count+" "+version
                int count = 0;
                final int v = arg.indexOf(' ');
                if (v == -1) {
                    version = "v1"; //$NON-NLS-1$
                    count = Integer.parseInt(arg);
                } else {
                    version = arg.substring(v + 1);
                    final String sc = arg.substring(0, v);
                    count = Integer.parseInt(sc);
                }
                notifyTestRunStarted(count);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_START)) {
                notifyTestStarted(arg);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_END)) {
                notifyTestEnded(arg);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_ERROR)) {
                extractFailure(arg, MessageIds.MessageStatus.ERROR);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_FAILED)) {
                extractFailure(arg, MessageIds.MessageStatus.FAILURE);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_RUN_END)) {
                final long elapsedTime = Long.parseLong(arg);
                testRunEnded(elapsedTime);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_STOPPED)) {
                final long elapsedTime = Long.parseLong(arg);
                notifyTestRunStopped(elapsedTime);
                shutDown();
                return this;
            }
            if (message.startsWith(MessageIds.TEST_TREE)) {
                notifyTestTreeEntry(arg);
                return this;
            }
            if (message.startsWith(MessageIds.TEST_RERAN)) {
                if (hasTestId())
                    scanReranMessage(arg);
                else
                    scanOldReranMessage(arg);
                return this;
            }
            return this;
        }
    }

    /**
     * Base class for states in which messages are appended to an internal
     * string buffer until an end message is read.
     */
    class AppendingProcessingState extends ProcessingState {
        private final StringBuffer fBuffer;
        private final String fEndString;


        AppendingProcessingState(final StringBuffer buffer, final String endString) {
            this.fBuffer = buffer;
            this.fEndString = endString;
        }


        @Override
        ProcessingState readMessage(final String message) {
            if (message.startsWith(fEndString)) {
                entireStringRead();
                return defaultState;
            }
            fBuffer.append(message);
            if (lastLineDelimiter != null)
                fBuffer.append(lastLineDelimiter);
            return this;
        }


        /**
         * subclasses can override to do special things when end message is read
         */
        void entireStringRead() {
            // No-op
        }
    }

    class TraceProcessingState extends AppendingProcessingState {
        TraceProcessingState() {
            super(failedTrace, MessageIds.TRACE_END);
        }


        @Override
        void entireStringRead() {
            notifyTestFailed();
            expectedResult.setLength(0);
            actualResult.setLength(0);
        }


        @Override
        ProcessingState readMessage(final String message) {
            if (message.startsWith(MessageIds.TRACE_END)) {
                notifyTestFailed();
                failedTrace.setLength(0);
                actualResult.setLength(0);
                expectedResult.setLength(0);
                return defaultState;
            }
            failedTrace.append(message);
            if (lastLineDelimiter != null)
                failedTrace.append(lastLineDelimiter);
            return this;
        }
    }

    /**
     * The failed trace that is currently reported from the RemoteTestRunner
     */
    private final StringBuffer failedTrace = new StringBuffer();
    /**
     * The expected test result
     */
    private final StringBuffer expectedResult = new StringBuffer();
    /**
     * The actual test result
     */
    private final StringBuffer actualResult = new StringBuffer();
    /**
     * The failed trace of a reran test
     */
    private final StringBuffer failedRerunTrace = new StringBuffer();

    ProcessingState defaultState = new DefaultProcessingState();
    ProcessingState traceState = new TraceProcessingState();
    ProcessingState expectedState = new AppendingProcessingState(expectedResult, MessageIds.EXPECTED_END);
    ProcessingState actualState = new AppendingProcessingState(actualResult, MessageIds.ACTUAL_END);
    ProcessingState rerunState = new AppendingProcessingState(failedRerunTrace, MessageIds.RTRACE_END);
    ProcessingState currentState = defaultState;

    /**
     * An array of listeners that are informed about test events.
     */
    private SubstepsRunListener[] listeners;

    /**
     * The server socket
     */
    private ServerSocket serverSocket;
    private Socket socket;
    private int port = -1;
    private PrintWriter writer;
    private PushbackReader pushbackReader;
    private String lastLineDelimiter;
    /**
     * The protocol version
     */
    private String version;
    /**
     * The failed test that is currently reported from the RemoteTestRunner
     */
    private String failedTest;
    /**
     * The Id of the failed test
     */
    private String failedTestId;
    /**
     * The kind of failure of the test that is currently reported as failed
     */
    private MessageStatus failureKind;

    private final boolean debug = false;

    /**
     * Reads the message stream from the RemoteTestRunner
     */
    private class ServerConnection extends Thread {
        int serverPort;


        public ServerConnection(final int port) {
            super("ServerConnection"); //$NON-NLS-1$
            this.serverPort = port;
        }


        @Override
        public void run() {
            try {
                if (debug)
                    System.out.println("Creating server socket " + serverPort); //$NON-NLS-1$
                serverSocket = new ServerSocket(serverPort);
                socket = serverSocket.accept();
                try {
                    pushbackReader = new PushbackReader(new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), "UTF-8"))); //$NON-NLS-1$
                } catch (final UnsupportedEncodingException e) {
                    pushbackReader = new PushbackReader(new BufferedReader(new InputStreamReader(
                            socket.getInputStream())));
                }
                try {
                    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); //$NON-NLS-1$
                } catch (final UnsupportedEncodingException e1) {
                    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                }
                String message;
                while (pushbackReader != null && (message = readMessage(pushbackReader)) != null)
                    receiveMessage(message);
            } catch (final SocketException e) {
                notifyTestRunTerminated();
            } catch (final IOException e) {
                FeatureRunnerPlugin.log(e);
                // fall through
            }
            shutDown();
        }
    }


    /**
     * Start listening to a test run. Start a server connection that the
     * RemoteTestRunner can connect to.
     * 
     * @param listeners
     *            listeners to inform
     * @param port
     *            port on which the server socket will be opened
     */
    public synchronized void startListening(final SubstepsRunListener[] l, final int p) {
        this.listeners = l;
        this.port = p;
        final ServerConnection connection = new ServerConnection(port);
        connection.start();
    }


    /**
     * Requests to stop the remote test run.
     */
    public synchronized void stopTest() {
        if (isRunning()) {
            writer.println(MessageIds.TEST_STOP);
            writer.flush();
        }
    }


    public synchronized void stopWaiting() {
        if (serverSocket != null && !serverSocket.isClosed() && socket == null) {
            shutDown(); // will throw a SocketException in Threads that wait in
                        // ServerSocket#accept()
        }
    }


    private synchronized void shutDown() {
        if (debug)
            System.out.println("shutdown " + port); //$NON-NLS-1$

        if (writer != null) {
            writer.close();
            writer = null;
        }
        try {
            if (pushbackReader != null) {
                pushbackReader.close();
                pushbackReader = null;
            }
        } catch (final IOException e) {
            // No-op
        }
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (final IOException e) {
            // No-op
        }
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (final IOException e) {
            // No-op
        }
    }


    public boolean isRunning() {
        return socket != null;
    }


    private String readMessage(final PushbackReader in) throws IOException {
        final StringBuffer buf = new StringBuffer(128);
        int ch;
        while ((ch = in.read()) != -1) {
            if (ch == '\n') {
                lastLineDelimiter = "\n"; //$NON-NLS-1$
                return buf.toString();
            } else if (ch == '\r') {
                ch = in.read();
                if (ch == '\n') {
                    lastLineDelimiter = "\r\n"; //$NON-NLS-1$
                } else {
                    in.unread(ch);
                    lastLineDelimiter = "\r"; //$NON-NLS-1$
                }
                return buf.toString();
            } else {
                buf.append((char) ch);
            }
        }
        lastLineDelimiter = null;
        if (buf.length() == 0)
            return null;
        return buf.toString();
    }


    private void receiveMessage(final String message) {
        this.currentState = currentState.readMessage(message);
    }


    private void scanOldReranMessage(final String arg) {
        // OLD V1 format
        // format: className" "testName" "status
        // status: FAILURE, ERROR, OK
        final int c = arg.indexOf(" "); //$NON-NLS-1$
        final int t = arg.indexOf(" ", c + 1); //$NON-NLS-1$
        final String className = arg.substring(0, c);
        final String testName = arg.substring(c + 1, t);
        final String status = arg.substring(t + 1);
        final String testId = className + testName;
        notifyTestReran(testId, className, testName, status);
    }


    private void scanReranMessage(final String arg) {
        // format: testId" "className" "testName" "status
        // status: FAILURE, ERROR, OK
        final int i = arg.indexOf(' ');
        final int c = arg.indexOf(' ', i + 1);
        final int t = arg.indexOf(' ', c + 1);
        final String testId = arg.substring(0, i);
        final String className = arg.substring(i + 1, c);
        final String testName = arg.substring(c + 1, t);
        final String status = arg.substring(t + 1);
        notifyTestReran(testId, className, testName, status);
    }


    private void notifyTestReran(final String testId, final String className, final String testName, final String status) {
        MessageStatus statusCode = MessageIds.MessageStatus.OK;
        if (status.equals("FAILURE")) //$NON-NLS-1$
            statusCode = MessageIds.MessageStatus.FAILURE;
        else if (status.equals("ERROR")) //$NON-NLS-1$
            statusCode = MessageIds.MessageStatus.ERROR;

        String trace = ""; //$NON-NLS-1$
        if (!statusCode.equals(MessageStatus.OK))
            trace = failedRerunTrace.toString();
        // assumption a rerun trace was sent before
        notifyTestReran(testId, className, testName, statusCode, trace);
    }


    private void extractFailure(final String arg, final MessageStatus status) {
        final String s[] = extractTestId(arg);
        failedTestId = s[0];
        failedTest = s[1];
        failureKind = status;
    }


    /**
     * @param arg
     *            test name
     * @return an array with two elements. The first one is the testId, the
     *         second one the testName.
     */
    String[] extractTestId(final String arg) {
        final String[] result = new String[2];
        if (!hasTestId()) {
            result[0] = arg; // use the test name as the test Id
            result[1] = arg;
            return result;
        }
        final int i = arg.indexOf(',');
        result[0] = arg.substring(0, i);
        result[1] = arg.substring(i + 1, arg.length());
        return result;
    }


    private boolean hasTestId() {
        if (version == null) // TODO fix me
            return true;
        return version.equals("v2"); //$NON-NLS-1$
    }


    private void notifyTestReran(final String testId, final String className, final String testName,
            final MessageStatus status, final String trace) {
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testReran(testId, className, testName, status.toStatus(), trace,
                            nullifyEmpty(expectedResult), nullifyEmpty(actualResult));
                }
            });
        }
    }


    private void notifyTestTreeEntry(final String treeEntry) {
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            if (!hasTestId())
                listener.testTreeEntry(fakeTestId(treeEntry));
            else
                listener.testTreeEntry(treeEntry);
        }
    }


    private String fakeTestId(final String treeEntry) {
        // extract the test name and add it as the testId
        final int index0 = treeEntry.indexOf(',');
        final String testName = treeEntry.substring(0, index0).trim();
        return testName + "," + treeEntry; //$NON-NLS-1$
    }


    private void notifyTestRunStopped(final long elapsedTime) {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testRunStopped(elapsedTime);
                }
            });
        }
    }


    private void testRunEnded(final long elapsedTime) {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testRunEnded(elapsedTime);
                }
            });
        }
    }


    private void notifyTestEnded(final String test) {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    final String s[] = extractTestId(test);
                    listener.testEnded(s[0], s[1]);
                }
            });
        }
    }


    private void notifyTestStarted(final String test) {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    final String s[] = extractTestId(test);
                    listener.testStarted(s[0], s[1]);
                }
            });
        }
    }


    private void notifyTestRunStarted(final int count) {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testRunStarted(count);
                }
            });
        }
    }


    private void notifyTestFailed() {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testFailed(failureKind.toStatus(), failedTestId, failedTest, failedTrace.toString(),
                            nullifyEmpty(expectedResult), nullifyEmpty(actualResult));
                }
            });
        }
    }


    /**
     * Returns a comparison result from the given buffer. Removes the
     * terminating line delimiter.
     * 
     * @param buf
     *            the comparison result
     * @return the result or <code>null</code> if empty
     * @since 3.7
     */
    private static String nullifyEmpty(final StringBuffer buf) {
        final int length = buf.length();
        if (length == 0)
            return null;

        final char last = buf.charAt(length - 1);
        if (last == '\n') {
            if (length > 1 && buf.charAt(length - 2) == '\r')
                return buf.substring(0, length - 2);
            return buf.substring(0, length - 1);
        } else if (last == '\r') {
            return buf.substring(0, length - 1);
        }
        return buf.toString();
    }


    private void notifyTestRunTerminated() {
        if (FeatureRunnerPlugin.instance().isStopped())
            return;
        for (int i = 0; i < listeners.length; i++) {
            final SubstepsRunListener listener = listeners[i];
            SafeRunner.run(new ListenerSafeRunnable() {
                @Override
                public void run() {
                    listener.testRunTerminated();
                }
            });
        }
    }


    public void rerunTest(final String testId, final String className, final String testName) {
        if (isRunning()) {
            actualResult.setLength(0);
            expectedResult.setLength(0);
            writer.println(MessageIds.TEST_RERUN + testId + " " + className + " " + testName); //$NON-NLS-1$ //$NON-NLS-2$
            writer.flush();
        }
    }
}
