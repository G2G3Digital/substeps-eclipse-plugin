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
