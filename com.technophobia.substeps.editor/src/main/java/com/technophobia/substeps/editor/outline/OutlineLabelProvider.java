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
package com.technophobia.substeps.editor.outline;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;

public class OutlineLabelProvider implements ILabelProvider {

    private static final Display DISPLAY = Display.getDefault();

    private final Map<Class<?>, Image> imageMap;


    public OutlineLabelProvider() {
        super();
        this.imageMap = new HashMap<Class<?>, Image>();
    }


    @Override
    public Image getImage(final Object element) {
        if (!imageMap.containsKey(element.getClass())) {
            imageMap.put(element.getClass(), loadImage(element));
        }
        return imageMap.get(element.getClass());
    }


    @Override
    public String getText(final Object element) {
        if (element instanceof AbstractModelElement) {
            final AbstractModelElement substepsElement = (AbstractModelElement) element;
            return substepsElement.getText();
        }
        return null;
    }


    @Override
    public void addListener(final ILabelProviderListener listener) {
        // No-op
    }


    @Override
    public void dispose() {
        // No-op
    }


    @Override
    public boolean isLabelProperty(final Object element, final String property) {
        return false;
    }


    @Override
    public void removeListener(final ILabelProviderListener listener) {
        // No-op
    }


    private Image loadImage(final Object element) {
        if (element instanceof AbstractModelElement) {
            final OutlineImage outlineImage = OutlineImage.findForModel((AbstractModelElement) element);
            if (outlineImage != null) {
                return loadImage(outlineImage.imagePath());
            }
        }
        return null;
    }


    private Image loadImage(final String filename) {
        return new Image(DISPLAY, OutlineLabelProvider.class.getResourceAsStream("/icons/" + filename));
    }
}
