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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.render.fake.FakeStepImplementationRendererTarget;

public class ParameterisedStepImplementationRendererTest {

    private StepImplementationToSuggestionRenderer stepRenderer;


    @Before
    public void intialise() {
        this.stepRenderer = new ParameterisedStepImplementationRenderer();
    }


    @Test
    public void nonParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation.parse("Given this is a non-parameterised step definition",
                FakeStepImplementationRendererTarget.class,
                FakeStepImplementationRendererTarget.class.getMethod("given"));
        assertTrue(stepRenderer.render(step).isMatch("Given this is a non-parameterised step definition"));
    }


    @Test
    public void singleParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation.parse(
                "Given this is a single parameter step definition with \"([^\"]*)\"",
                FakeStepImplementationRendererTarget.class,
                FakeStepImplementationRendererTarget.class.getMethod("given", String.class));
        assertTrue(stepRenderer.render(step).isMatch(
                "Given this is a single parameter step definition with \"A Value\""));
    }


    @Test
    public void multiParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation
                .parse("Given this is a multi parameter step definition with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"",
                        FakeStepImplementationRendererTarget.class, FakeStepImplementationRendererTarget.class
                                .getMethod("given", String.class, String.class, String.class));
        assertTrue(stepRenderer.render(step).isMatch(
                "Given this is a multi parameter step definition with \"Value 1\", \"Value 2\" and \"Value 3\""));
    }
}
