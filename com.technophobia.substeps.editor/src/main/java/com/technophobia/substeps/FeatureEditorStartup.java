package com.technophobia.substeps;

import org.eclipse.ui.IStartup;

public class FeatureEditorStartup implements IStartup {

    // Marker class to force substeps plugin to load as application starts

    @Override
    public void earlyStartup() {
        FeatureEditorPlugin.instance().getProjectManager().workspaceLoaded();
    }

}
