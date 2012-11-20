package com.technophobia.substeps.editor.preferences.page;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class SubstepsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final ProjectManager projectManager;


    public SubstepsPreferencePage() {
        super(GRID);
        this.projectManager = FeatureEditorPlugin.instance().getProjectManager();
    }


    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(FeatureEditorPlugin.instance().getPreferenceStore());
        setDescription("Substeps preferences");
    }


    @Override
    public boolean performOk() {
        final boolean result = super.performOk();
        updateProjects();
        return result;
    }


    @Override
    protected void performApply() {
        super.performApply();

        updateProjects();
    }


    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(SubstepsPreferences.ENABLE_PROBLEMS.key(), "&Enable substeps problems",
                getFieldEditorParent()));
    }


    private void updateProjects() {
        for (final IProject project : affectedProjects()) {
            projectManager.preferencesChanged(project);
        }
    }


    private IProject[] affectedProjects() {
        return ResourcesPlugin.getWorkspace().getRoot().getProjects();
    }
}
