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
