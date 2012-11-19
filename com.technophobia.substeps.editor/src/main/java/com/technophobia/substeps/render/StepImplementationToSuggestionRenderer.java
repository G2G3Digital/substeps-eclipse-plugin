package com.technophobia.substeps.render;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.step.Suggestion;

/**
 * Get a string representation of a {@link StepImplementation}
 * 
 * @author sforbes
 * 
 */
public interface StepImplementationToSuggestionRenderer {

    /**
     * Convert stepImplementation to Suggestion
     * 
     * @param stepImplementation
     *            the step to be rendered
     * @return the rendered Suggestion
     */
    Suggestion render(StepImplementation stepImplementation);
}
