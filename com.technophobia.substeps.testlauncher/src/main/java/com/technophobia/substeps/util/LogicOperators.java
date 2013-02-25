package com.technophobia.substeps.util;

public class LogicOperators {

    public static boolean not(final boolean value) {
        return !value;
    }


    public static boolean and(final boolean... values) {
        for (final boolean value : values) {
            if (!value) {
                return false;
            }
        }
        return true;
    }


    public static boolean or(final boolean... values) {
        for (final boolean value : values) {
            if (value) {
                return true;
            }
        }
        return false;
    }
}
