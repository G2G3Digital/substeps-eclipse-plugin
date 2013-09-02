package com.technophobia.substeps.junit.launcher;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Supplier;

public class FeatureEditorProjectManagerSupplier implements Supplier<ProjectManager> {

    @Override
    public ProjectManager get() {
        return FeatureEditorPlugin.instance().projectManager();
    }

}
