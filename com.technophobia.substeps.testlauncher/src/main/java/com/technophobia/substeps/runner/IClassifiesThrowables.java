package com.technophobia.substeps.runner;

public interface IClassifiesThrowables {

    boolean isComparisonFailure(Throwable throwable);


    String getTrace(Throwable t);
}
