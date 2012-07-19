package com.technophobia.eclipse.ui;

public interface Notifier<T> {

    void notify(T t);


    T currentValue();
}
