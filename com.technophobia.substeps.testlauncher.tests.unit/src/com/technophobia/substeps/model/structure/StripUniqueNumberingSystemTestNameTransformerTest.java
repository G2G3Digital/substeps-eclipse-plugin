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

public class StripUniqueNumberingSystemTestNameTransformerTest {

    private Transformer<String, String> transformer;


    @Before
    public void initialise() {
        this.transformer = new StripUniqueNumberingSystemTestNameTransformer();
    }


    @Test
    public void removesUniqueNumberingFromTestName() {

        assertThat(transformer.from("1: Some input"), is("Some input"));

        assertThat(transformer.from("1-2: Some input 2"), is("Some input 2"));

        assertThat(transformer.from("3-1-2-1: Some input 3"), is("Some input 3"));
    }


    @Test
    public void nameWithNoUniqueNumberingRemainsTheSame() {
        assertThat(transformer.from("Some input 4"), is("Some input 4"));
    }
}
