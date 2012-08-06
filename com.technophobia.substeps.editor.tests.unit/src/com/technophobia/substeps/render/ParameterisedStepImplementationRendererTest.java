package com.technophobia.substeps.render;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.render.fake.FakeStepImplementationRendererTarget;

public class ParameterisedStepImplementationRendererTest {

    private StepImplementationRenderer stepRenderer;


    @Before
    public void intialise() {
        this.stepRenderer = new ParameterisedStepImplementationRenderer();
    }


    @Test
    public void nonParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation.parse("Given this is a non-parameterised step definition",
                FakeStepImplementationRendererTarget.class,
                FakeStepImplementationRendererTarget.class.getMethod("given"));
        assertThat(stepRenderer.render(step), is("Given this is a non-parameterised step definition"));
    }


    @Test
    public void singleParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation.parse(
                "Given this is a single parameter step definition with \"([^\"]*)\"",
                FakeStepImplementationRendererTarget.class,
                FakeStepImplementationRendererTarget.class.getMethod("given", String.class));
        assertThat(stepRenderer.render(step), is("Given this is a single parameter step definition with \"<value>\""));
    }


    @Test
    public void multiParameterisedStepImplementationsRenderCorrectly() throws Exception {
        final StepImplementation step = StepImplementation
                .parse("Given this is a multi parameter step definition with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"",
                        FakeStepImplementationRendererTarget.class, FakeStepImplementationRendererTarget.class
                                .getMethod("given", String.class, String.class, String.class));
        assertThat(stepRenderer.render(step),
                is("Given this is a multi parameter step definition with \"<value>\", \"<value>\" and \"<value>\""));
    }
}
