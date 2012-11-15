package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.technophobia.substeps.supplier.Transformer;

@SuppressWarnings("restriction")
public class ImageDescriptorImageLoader implements Transformer<SubstepsIcon, Image> {

    @Override
    public Image from(final SubstepsIcon icon) {
        final ImageDescriptor descriptor = descriptorFor(icon);

        Image image = descriptor.createImage();
        if (image == null) {
            image = ImageDescriptor.getMissingImageDescriptor().createImage();
        }
        return image;
    }


    private ImageDescriptor descriptorFor(final SubstepsIcon icon) {
        return JUnitPlugin.getImageDescriptor(icon.getPath());
    }

}
