/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
