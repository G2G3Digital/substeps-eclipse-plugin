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
