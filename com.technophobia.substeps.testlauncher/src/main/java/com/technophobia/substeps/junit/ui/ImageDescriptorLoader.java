package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.resource.ImageDescriptor;

import com.technophobia.eclipse.transformer.Transformer;

@SuppressWarnings("restriction")
public class ImageDescriptorLoader implements Transformer<SubstepsIcon, ImageDescriptor> {
    @Override
    public ImageDescriptor to(final SubstepsIcon icon) {
        return JUnitPlugin.getImageDescriptor(icon.getPath());
    }
}
