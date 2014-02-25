package com.technophobia.substeps.nature;

public interface CompatibilityChecker<T> {

    boolean isCompatible(T t);


    void markResourceAsCompatibilityChecked(T t);
}
