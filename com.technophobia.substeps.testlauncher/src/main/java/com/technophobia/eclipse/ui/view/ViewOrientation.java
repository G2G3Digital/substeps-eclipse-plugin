package com.technophobia.eclipse.ui.view;

public enum ViewOrientation {
    VIEW_ORIENTATION_VERTICAL(0), //
    VIEW_ORIENTATION_HORIZONTAL(1), //
    VIEW_ORIENTATION_AUTOMATIC(2);

    private final int value;


    private ViewOrientation(final int value) {
        this.value = value;
    }


    public int value() {
        return value;
    }


    public static ViewOrientation forValue(final int value) {
        for (final ViewOrientation vo : values()) {
            if (vo.value == value) {
                return vo;
            }
        }
        return null;
    }
}
