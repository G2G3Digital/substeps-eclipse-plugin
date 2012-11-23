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
package com.technophobia.substeps.model.structure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.supplier.Transformer;

public class ReplacesConstantWithAnotherConstantValueTestNameTransformerTest {

    private Transformer<String, String> transformer;


    @Before
    public void initialise() {
        this.transformer = new ReplacesConstantWithAnotherConstantValueTestNameTransformer("original", "new");
    }


    @Test
    public void replacesOriginalValueCorrectly() {
        assertThat(transformer.from("original"), is("new"));
    }


    @Test
    public void maintainsValueWhenNotRequiringReplacement() {
        assertThat(transformer.from("different"), is("different"));
    }
}
