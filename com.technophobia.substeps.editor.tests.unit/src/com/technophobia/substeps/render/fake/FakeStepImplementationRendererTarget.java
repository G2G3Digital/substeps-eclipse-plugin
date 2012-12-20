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
package com.technophobia.substeps.render.fake;

import com.technophobia.substeps.model.SubSteps.Step;

public class FakeStepImplementationRendererTarget {

    @Step("Given this is a non-parameterised step definition")
    public void given() {
        // no-op
    }


    @SuppressWarnings("unused")
    @Step("Given this is a single parameter step definition with \"([^\"]*)\"")
    public void given(final String parameter) {
        // no-op
    }


    @SuppressWarnings("unused")
    @Step("Given this is a multi-parameter step definition with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"")
    public void given(final String parameter1, final String parameter2, final String parameter3) {
        // no-op
    }
}
