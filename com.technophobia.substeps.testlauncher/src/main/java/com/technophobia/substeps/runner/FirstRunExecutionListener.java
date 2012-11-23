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
