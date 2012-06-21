package com.technophobia.substeps.junit.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import com.technophobia.eclipse.transformer.Transformer;
import com.technophobia.eclipse.ui.Disposable;

public class SubstepsIconProvider implements Disposable {

    private final Map<SubstepsIcon, Image> icons;
    private final Transformer<SubstepsIcon, Image> imageLoader;


    public SubstepsIconProvider(final Transformer<SubstepsIcon, Image> imageLoader) {
        this.imageLoader = imageLoader;
        this.icons = new HashMap<SubstepsIcon, Image>();
    }


    public Image imageFor(final SubstepsIcon icon) {
        if (!icons.containsKey(icon)) {
            icons.put(icon, createImageFor(icon));
        }
        return icons.get(icon);
    }


    @Override
    public void dispose() {
        for (final Image image : icons.values()) {
            image.dispose();
        }
    }


    private Image createImageFor(final SubstepsIcon iconToLoad) {
        return imageLoader.to(iconToLoad);
    }
}
