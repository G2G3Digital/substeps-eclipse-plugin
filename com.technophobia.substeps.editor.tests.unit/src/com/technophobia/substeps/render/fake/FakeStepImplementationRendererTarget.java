package com.technophobia.substeps.render.fake;

import com.technophobia.substeps.model.SubSteps.Step;

@SuppressWarnings("unused")
public class FakeStepImplementationRendererTarget {

    @Step("Given this is a non-parameterised step definition")
    public void given() {
        // no-op
    }


    @Step("Given this is a single parameter step definition with \"([^\"]*)\"")
    public void given(final String parameter) {
        // no-op
    }


    @Step("Given this is a multi-parameter step definition with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"")
    public void given(final String parameter1, final String parameter2, final String parameter3) {
        // no-op
    }
}
