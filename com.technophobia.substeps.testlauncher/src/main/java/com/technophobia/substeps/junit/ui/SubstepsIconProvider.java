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
package com.technophobia.substeps.junit.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.technophobia.eclipse.ui.Disposable;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepsIconProvider implements Disposable {

    private final Map<SubstepsIcon, Image> icons;
    private final Transformer<SubstepsIcon, Image> imageLoader;
    private final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorLoader;


    public SubstepsIconProvider(final Transformer<SubstepsIcon, Image> imageLoader,
            final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorLoader) {
        this.imageLoader = imageLoader;
        this.imageDescriptorLoader = imageDescriptorLoader;
        this.icons = new HashMap<SubstepsIcon, Image>();
    }


    public Image imageFor(final SubstepsIcon icon) {
        if (!icons.containsKey(icon)) {
            icons.put(icon, createImageFor(icon));
        }
        return icons.get(icon);
    }


    public ImageDescriptor imageDescriptorFor(final SubstepsIcon icon) {
        return this.imageDescriptorLoader.from(icon);
    }


    @Override
    public void dispose() {
        for (final Image image : icons.values()) {
            image.dispose();
        }
    }


    private Image createImageFor(final SubstepsIcon iconToLoad) {
        return imageLoader.from(iconToLoad);
    }
}
