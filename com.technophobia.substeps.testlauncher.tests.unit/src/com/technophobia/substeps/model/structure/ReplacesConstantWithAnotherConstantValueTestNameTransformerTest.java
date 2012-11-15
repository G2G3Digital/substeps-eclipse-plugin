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
