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