package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.resource.ImageDescriptor;

import com.technophobia.substeps.supplier.Transformer;

@SuppressWarnings("restriction")
public class ImageDescriptorLoader implements Transformer<SubstepsIcon, ImageDescriptor> {
    @Override
    public ImageDescriptor from(final SubstepsIcon icon) {
        return JUnitPlugin.getImageDescriptor(icon.getPath());
    }
}
