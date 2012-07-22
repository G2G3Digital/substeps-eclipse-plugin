package com.technophobia.substeps.render;

import com.technophobia.substeps.model.StepImplementation;

/**
 * For rendering during content assist - render the {@link StepImplementation},
 * replacing any regex text with the string <value>
 * 
 * @author sforbes
 * 
 */
public class ParameterisedStepImplementationRenderer implements StepImplementationRenderer {

    @Override
    public String render(final StepImplementation stepImplementation) {
        // replace any regex's with parameter names
        if (stepImplementation.getMethod().getParameterTypes() != null
                && stepImplementation.getMethod().getParameterTypes().length > 0) {
            // tokens to be replaced
            return replaceRegExParams(stepImplementation.getValue(), "value");
        }

        return stepImplementation.getValue();
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
