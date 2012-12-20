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

public class FailedComparison {

    private final String expected;
    private final String actual;


    public FailedComparison(final String expected, final String actual) {
        this.expected = expected;
        this.actual = actual;
    }


    public String getActual() {
        return actual;
    }


    public String getExpected() {
        return expected;
    }


    void sendMessages(final MessageSender sender) {
        sender.sendMessage(MessageIds.EXPECTED_START);
        sender.sendMessage(getExpected());
        sender.sendMessage(MessageIds.EXPECTED_END);

        sender.sendMessage(MessageIds.ACTUAL_START);
        sender.sendMessage(getActual());
        sender.sendMessage(MessageIds.ACTUAL_END);
    }

}
