package com.technophobia.substeps.runner;

import com.technophobia.substeps.model.MessageIds;
import com.technophobia.substeps.runner.RemoteTestRunner.ReranStatus;

public class RerunExecutionListener extends FirstRunExecutionListener {
    // Don't send ids here, since they don't match the ids of the original run:
    // RemoteTestRunner#rerunTest(..) reloads Test, so ITestReferences are not
    // equals(..).

    public RerunExecutionListener(final MessageSender sender, final TestIdMap ids) {
        super(sender, ids);
    }

    private ReranStatus status = ReranStatus.OK;


    @Override
    public void notifyTestFailed(final TestReferenceFailure failure) {
        sendFailure(failure, MessageIds.RTRACE_START, MessageIds.RTRACE_END);

        final String statusStr = failure.getStatus();
        if (statusStr.equals(MessageIds.TEST_FAILED))
            status = ReranStatus.FAILURE;
        else if (statusStr.equals(MessageIds.TEST_ERROR))
            status = ReranStatus.ERROR;
        else
            throw new IllegalArgumentException(statusStr);
    }


    @Override
    public void notifyTestStarted(final ITestIdentifier test) {
        // do nothing
    }


    @Override
    public void notifyTestEnded(final ITestIdentifier test) {
        // do nothing
    }


    public ReranStatus getStatus() {
        return status;
    }

}
