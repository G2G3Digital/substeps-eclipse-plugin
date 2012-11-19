package com.technophobia.substeps.syntax;

import java.io.File;

import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;

public class NullSyntaxErrorReporter implements SyntaxErrorReporter {

    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final String description)
            throws RuntimeException {
        // No-op
    }


    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        // No-op
    }


    @Override
    public void reportSubstepsError(final File file, final String line, final int lineNumber, final String description)
            throws RuntimeException {
        // No-op
    }


    @Override
    public void reportSubstepsError(final File file, final String line, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        // No-op
    }

}
