package com.technophobia.substeps.syntax;

import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;

public interface DeferredReportingSyntaxErrorReporter extends SyntaxErrorReporter {

    void applyChanges();
}
