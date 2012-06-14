package com.technophobia.eclipse.transformer;

public interface Decorator<T, With> {

    void decorate(T t, With with);
}
