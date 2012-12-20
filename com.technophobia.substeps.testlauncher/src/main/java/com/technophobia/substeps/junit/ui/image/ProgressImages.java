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
package com.technophobia.substeps.junit.ui.image;

import org.eclipse.swt.graphics.Image;

import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsProgressIcon;

/**
 * Manages a set of images that can show progress in the image itself.
 */
public class ProgressImages {
    private static final int PROGRESS_STEPS = 9;

    private final Image[] okImages = new Image[PROGRESS_STEPS];
    private final Image[] failureImages = new Image[PROGRESS_STEPS];

    private final SubstepsIconProvider iconProvider;


    public ProgressImages(final SubstepsIconProvider iconProvider) {
        this.iconProvider = iconProvider;

    }


    public void dispose() {
        if (!isLoaded())
            return;

        for (int i = 0; i < PROGRESS_STEPS; i++) {
            okImages[i].dispose();
            okImages[i] = null;
            failureImages[i].dispose();
            failureImages[i] = null;
        }
    }


    public Image getImage(final int current, final int total, final int errors, final int failures) {
        if (!isLoaded())
            load();

        if (total == 0)
            return okImages[0];
        int index = ((current * PROGRESS_STEPS) / total) - 1;
        index = Math.min(Math.max(0, index), PROGRESS_STEPS - 1);

        if (errors + failures == 0)
            return okImages[index];
        return failureImages[index];
    }


    private boolean isLoaded() {
        return okImages[0] != null;
    }


    private void load() {
        if (isLoaded())
            return;

        for (int i = 0; i < PROGRESS_STEPS; i++) {
            okImages[i] = createImage(SubstepsProgressIcon.valueOf("OK" + i));
            failureImages[i] = createImage(SubstepsProgressIcon.valueOf("FAILURE" + i));
        }
    }


    private Image createImage(final SubstepsIcon icon) {
        return iconProvider.imageFor(icon);
    }
}
