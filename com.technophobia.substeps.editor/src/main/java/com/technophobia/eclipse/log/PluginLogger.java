package com.technophobia.eclipse.log;

public interface PluginLogger {

    void info(String message);


    void warn(String message);


    void warn(String message, Throwable ex);


    void error(String message);


    void error(String message, Throwable ex);
}
