package com.technophobia.substeps.junit.launcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectManagerSuppliedSubstepsLocationFinder implements Transformer<IProject, String> {

    private final Supplier<ProjectManager> projectManagerSupplier;


    public ProjectManagerSuppliedSubstepsLocationFinder() {
        this(new FeatureEditorProjectManagerSupplier());
    }


    public ProjectManagerSuppliedSubstepsLocationFinder(final Supplier<ProjectManager> projectManagerSupplier) {
        this.projectManagerSupplier = projectManagerSupplier;
    }


    @Override
    public String from(final IProject project) {
        final ProjectManager projectManager = projectManagerSupplier.get();

        final IPath projectPath = project.getLocation();
        final IPath substepsPath = projectManager.substepsFolderFor(project);

        if (!projectPath.isPrefixOf(substepsPath)) {
            return null;
        }
        return substepsPath.toOSString().substring(projectPath.toOSString().length() + 1);
    }

}
