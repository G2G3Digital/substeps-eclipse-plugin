package com.technophobia.substeps.util;

import static com.technophobia.substeps.util.LogicOperators.and;
import static com.technophobia.substeps.util.LogicOperators.not;
import static com.technophobia.substeps.util.LogicOperators.or;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LogicOperatorsTest {

    @Test
    public void notTests() {
        assertFalse(not(true));
        assertTrue(not(false));
    }


    @Test
    public void andTests() {
        assertFalse(and(false, false, false));
        assertFalse(and(false, false, true));
        assertFalse(and(true, true, false));
        assertTrue(and(true, true, true));
    }


    @Test
    public void orTests() {
        assertFalse(or(false, false, false));
        assertTrue(or(true, false, false));
        assertTrue(or(true, true, false));
        assertTrue(or(true, true, true));
    }
}
