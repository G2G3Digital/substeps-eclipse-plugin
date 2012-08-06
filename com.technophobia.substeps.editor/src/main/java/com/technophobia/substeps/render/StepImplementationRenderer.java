package com.technophobia.substeps.render;

import com.technophobia.substeps.model.StepImplementation;

/**
 * Get a string representation of a {@link StepImplementation}
 * 
 * @author sforbes
 * 
 */
public interface StepImplementationRenderer {

    /**
     * Convert stepImplementation to string
     * 
     * @param stepImplementation
     *            the step to be rendered
     * @return the rendered step
     */
    String render(StepImplementation stepImplementation);
}
