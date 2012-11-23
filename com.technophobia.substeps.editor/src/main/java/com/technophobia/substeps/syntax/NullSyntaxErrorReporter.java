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
package com.technophobia.substeps.syntax;

import java.io.File;

import com.technophobia.substeps.model.exception.StepImplementationException;
import com.technophobia.substeps.model.exception.SubstepsParsingException;
import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;

public class NullSyntaxErrorReporter implements SyntaxErrorReporter {

    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final int offset,
            final String description) throws RuntimeException {
        // No-op
    }


    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final int offset,
            final String description, final RuntimeException ex) throws RuntimeException {
        // No-op
    }


    @Override
    public void reportStepImplError(final StepImplementationException ex) {
        // No-op
    }


    @Override
    public void reportSubstepsError(final SubstepsParsingException ex) {
        // No-op
    }
}
