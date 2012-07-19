package com.technophobia.substeps.runner;

public interface MessageSender {

    void sendMessage(String msg);


    void flush();
}
