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
