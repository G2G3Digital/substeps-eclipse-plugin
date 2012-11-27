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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.technophobia.eclipse.project.ProjectObserver;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.preferences.OverridableProjectLocalPreferenceStore;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class SubstepsPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    private FieldEditor controlEditor;
    private final String pluginId;
    private final List<FieldEditor> fieldEditors;
    private final Map<FieldEditor, Composite> fieldToParentMap;

    private final ProjectObserver projectObserver;


    public SubstepsPropertyPage() {
        this.fieldEditors = new ArrayList<FieldEditor>();
        this.fieldToParentMap = new HashMap<FieldEditor, Composite>();
        this.projectObserver = FeatureEditorPlugin.instance().getProjectObserver();
        this.pluginId = FeatureEditorPlugin.PLUGIN_ID;
    }


    @Override
    public boolean performOk() {
        final boolean result = super.performOk();

        controlEditor.store();
        for (final FieldEditor fieldEditor : fieldEditors) {
            fieldEditor.store();
        }
        savePreferenceStore();

        updateProject();

        return result;
    }


    @Override
    public void setElement(final IAdaptable element) {
        super.setElement(element);

        setPreferenceStore(createResourceLocalPreferenceStore((IProject) element.getAdapter(IProject.class),
                (IPersistentPreferenceStore) FeatureEditorPlugin.instance().getPreferenceStore()));
    }


    @Override
    protected Control createContents(final Composite parent) {
        final Composite composite = createComposite(parent);

        createProjectOverrideControl(composite);

        createProblemsGroup(composite);
        createFoldersGroup(composite);

        final boolean projectOverride = getPreferenceStore().getBoolean(SubstepsPreferences.PROJECT_OVERRIDE.key());
        setFieldsEnabled(projectOverride);

        return composite;
    }


    @Override
    protected void performApply() {
        super.performApply();

        savePreferenceStore();
        updateProject();
    }


    protected void savePreferenceStore() {
        try {
            ((IPersistentPreferenceStore) getPreferenceStore()).save();
        } catch (final IOException ex) {
            FeatureEditorPlugin.instance().error("Could not save preferences", ex);
        }
    }


    protected void setFieldsEnabled(final boolean enabled) {
        for (final FieldEditor field : fieldEditors) {
            field.setEnabled(enabled, fieldToParentMap.get(field));
        }
    }


    private void createProjectOverrideControl(final Composite composite) {
        controlEditor = new BooleanFieldEditor(SubstepsPreferences.PROJECT_OVERRIDE.key(),
                "&Enable project specific settings", composite);
        controlEditor.setPage(this);
        controlEditor.setPreferenceStore(getPreferenceStore());
        controlEditor.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent event) {
                final boolean isSelected = ((Boolean) event.getNewValue()).booleanValue();
                setFieldsEnabled(isSelected);
            }
        });
        controlEditor.load();

    }


    private void createProblemsGroup(final Composite composite) {
        final Group group = new Group(composite, SWT.NONE);
        group.setFont(composite.getFont());
        group.setText("Problems");
        final GridData layoutData = new GridData(GridData.FILL, GridData.FILL, true, false);
        layoutData.verticalIndent = 20;
        group.setLayoutData(layoutData);

        group.setLayout(new GridLayout(1, false));

        final FieldEditor enableProblems = new BooleanFieldEditor(SubstepsPreferences.ENABLE_PROBLEMS.key(),
                "&Show substeps problems", group);
        enableProblems.setPage(this);
        enableProblems.setPreferenceStore(getPreferenceStore());
        enableProblems.load();
        addField(enableProblems, group);
    }


    private void createFoldersGroup(final Composite composite) {
        final Group group = new Group(composite, SWT.NONE);
        group.setFont(composite.getFont());
        group.setText("Folders");
        final GridData layoutData = new GridData(GridData.FILL, GridData.FILL, true, false);
        layoutData.verticalIndent = 20;
        group.setLayoutData(layoutData);

        group.setLayout(new GridLayout(1, false));

        addField(createStringFieldEditor(SubstepsPreferences.FEATURE_FOLDER, "&Feature folder", group), group);
        addField(createStringFieldEditor(SubstepsPreferences.SUBSTEPS_FOLDER, "&Substeps folder", group), group);
    }


    private FieldEditor createStringFieldEditor(final SubstepsPreferences preference, final String label,
            final Group group) {
        final StringButtonFieldEditor fieldEditor = new StringButtonFieldEditor(preference.key(), label, group) {

            @Override
            protected String changePressed() {
                final String newLocation = handleBrowseFolderClick();
                return newLocation != null ? newLocation : "";
            }
        };
        fieldEditor.setChangeButtonText("Browse");
        fieldEditor.setPage(this);
        fieldEditor.setPreferenceStore(getPreferenceStore());
        fieldEditor.load();
        return fieldEditor;
    }


    private Composite createComposite(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        return composite;
    }


    private void addField(final FieldEditor field, final Composite parent) {
        fieldEditors.add(field);
        fieldToParentMap.put(field, parent);
    }


    private IPersistentPreferenceStore createResourceLocalPreferenceStore(final IProject project,
            final IPersistentPreferenceStore preferenceStore) {
        return new OverridableProjectLocalPreferenceStore(pluginId, SubstepsPreferences.PROJECT_OVERRIDE.key(), "true",
                project, preferenceStore);
    }


    private void updateProject() {
        final IProject project = (IProject) getElement().getAdapter(IProject.class);

        projectObserver.preferencesChanged(project);
    }


    private String handleBrowseFolderClick() {
        final IResource resource = chooseFolderResource();
        if (resource == null) {
            return "";
        }

        return projectLocalisedPathFor(resource);
    }


    private IResource chooseFolderResource() {
        final ILabelProvider labelProvider = new WorkbenchLabelProvider();
        final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), labelProvider,
                new BaseWorkbenchContentProvider());
        dialog.setTitle("Choose folder");
        dialog.setMessage("Please select a folder");
        dialog.setInput(getElement().getAdapter(IProject.class));
        dialog.setAllowMultiple(false);
        dialog.setValidator(new ISelectionStatusValidator() {

            @Override
            public IStatus validate(final Object[] selection) {
                if (selection.length > 0) {
                    final Object item = selection[0];
                    if (item instanceof IFolder) {
                        return new Status(IStatus.OK, FeatureEditorPlugin.PLUGIN_ID, "");
                    }
                }
                return new Status(IStatus.ERROR, FeatureEditorPlugin.PLUGIN_ID, "Not a folder");
            }
        });

        if (dialog.open() == Window.OK) {
            return (IResource) dialog.getFirstResult();
        }
        return null;
    }


    /**
     * Convert a resource to its os-specific project localised path string
     * 
     * @param resource
     *            to be localised
     * @return localised path
     */
    protected String projectLocalisedPathFor(final IResource resource) {
        return resource.getFullPath().removeFirstSegments(1).toOSString();
    }
}
