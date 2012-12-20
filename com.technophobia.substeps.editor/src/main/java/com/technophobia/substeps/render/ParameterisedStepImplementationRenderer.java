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
package com.technophobia.substeps.render;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.step.PatternSuggestion;
import com.technophobia.substeps.step.Suggestion;

/**
 * For rendering during content assist - render the {@link StepImplementation},
 * replacing any regex text with the string <value>
 * 
 * @author sforbes
 * 
 */
public class ParameterisedStepImplementationRenderer implements StepImplementationToSuggestionRenderer {

    @Override
    public Suggestion render(final StepImplementation stepImplementation) {
        // replace any regex's with parameter names

        if (stepImplementation.getMethod().getParameterTypes() != null
                && stepImplementation.getMethod().getParameterTypes().length > 0) {
            // tokens to be replaced
            return patternSuggestion(stepImplementation);
        }

        return new Suggestion(stepImplementation.getValue());
    }


    public Suggestion patternSuggestion(final StepImplementation stepImplementation) {
        return new PatternSuggestion(stepImplementation.getValue(), replaceRegExParams(stepImplementation.getValue(),
                "value"));
    }


    /**
     * Find any regex, and replace
     * 
     * @param line
     *            The unprocessed line
     * @param replacementText
     *            The text to replace regex
     * @return The processed line
     */
    public String replaceRegExParams(final String line, final String replacementText) {

        String out = line;
        // replace the params with a reg ex, a quoted and non quoted
        // variant
        out = out.replaceAll("\\([^\\)]*\\)", "<" + replacementText + ">");

        // remove out any ? indicating options in the regex
        out = out.replaceAll("\\?", "");
        out = out.replaceAll("\\\\", "");

        return out;
    }

}
