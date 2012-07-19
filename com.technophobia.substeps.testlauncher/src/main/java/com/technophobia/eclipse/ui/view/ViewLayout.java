package com.technophobia.eclipse.ui.view;


public enum ViewLayout {

    FLAT(0), //
    HIERARCHICAL(1);

    private final int value;


    private ViewLayout(final int value) {
        this.value = value;
    }


    public int value() {
        return value;
    }


    public static ViewLayout forValue(final int value) {
        for (final ViewLayout vl : values()) {
            if (vl.value == value) {
                return vl;
            }
        }
        return null;
    }
}
