package com.technophobia.substeps.editor;

import org.osgi.framework.Bundle;

import com.technophobia.substeps.FeatureEditorPlugin;

public class EditorActivatorBundleSupplier { // implements BundleSupplier {

	// @Override
	public Bundle get() {
		return FeatureEditorPlugin.instance().getBundleContext().getBundle();
	}

}
