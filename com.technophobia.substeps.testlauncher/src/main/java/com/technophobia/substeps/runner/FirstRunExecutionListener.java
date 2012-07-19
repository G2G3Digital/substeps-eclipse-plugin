package com.technophobia.substeps.runner;

import com.technophobia.substeps.model.MessageIds;

public class FirstRunExecutionListener implements IListensToTestExecutions {
    protected MessageSender sender;

    private final TestIdMap ids;


    FirstRunExecutionListener(final MessageSender sender, final TestIdMap ids) {
        this.sender = sender;
        if (ids == null)
            throw new NullPointerException();
        this.ids = ids;
    }


    @Override
    public void notifyTestEnded(final ITestIdentifier test) {
        sendMessage(test, MessageIds.TEST_END);
    }


    @Override
    public void notifyTestFailed(final TestReferenceFailure failure) {
        sendMessage(failure.getTest(), failure.getStatus());
        sendFailure(failure, MessageIds.TRACE_START, MessageIds.TRACE_END);
    }


    @Override
    public void notifyTestStarted(final ITestIdentifier test) {
        sendMessage(test, MessageIds.TEST_START);
        sender.flush();
    }


    private String getTestId(final ITestIdentifier test) {
        return ids.getTestId(test);
    }


    protected void sendFailure(final TestReferenceFailure failure, final String startTrace, final String endTrace) {
        final FailedComparison comparison = failure.getComparison();
        if (comparison != null)
            comparison.sendMessages(sender);

        sender.sendMessage(startTrace);
        sender.sendMessage(failure.getTrace());
        sender.sendMessage(endTrace);
        sender.flush();
    }


    private void sendMessage(final ITestIdentifier test, final String status) {
        sender.sendMessage(status + getTestId(test) + ',' + test.getName());
    }

}
