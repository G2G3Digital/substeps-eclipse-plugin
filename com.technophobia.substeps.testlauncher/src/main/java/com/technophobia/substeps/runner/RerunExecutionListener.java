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
