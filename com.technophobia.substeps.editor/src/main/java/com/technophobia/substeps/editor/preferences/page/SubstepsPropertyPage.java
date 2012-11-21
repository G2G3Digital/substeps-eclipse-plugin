package com.technophobia.substeps.editor.preferences.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.preferences.ResourceLocalPreferenceStore;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class SubstepsPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    private final List<FieldEditor> fieldEditors;


    public SubstepsPropertyPage() {
        this.fieldEditors = new ArrayList<FieldEditor>();
    }


    @Override
    protected Control createContents(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);

        final FieldEditor enableProblems = new BooleanFieldEditor(SubstepsPreferences.ENABLE_PROBLEMS.key(),
                "&Enable substeps problems", composite);
        enableProblems.setPage(this);
        enableProblems.setPreferenceStore(getPreferenceStore());
        enableProblems.load();
        fieldEditors.add(enableProblems);

        return composite;
    }


    @Override
    public boolean performOk() {
        final boolean result = super.performOk();

        for (final FieldEditor fieldEditor : fieldEditors) {
            fieldEditor.store();
        }

        return result;
    }


    @Override
    public void setElement(final IAdaptable element) {
        super.setElement(element);

        setPreferenceStore(createResourceLocalPreferenceStore(element.getAdapter(IResource.class), FeatureEditorPlugin
                .instance().getPreferenceStore()));
    }


    private IPersistentPreferenceStore createResourceLocalPreferenceStore(final IResource resource,
            final IPreferenceStore preferenceStore) {
        return new ResourceLocalPreferenceStore(resource, preferenceStore);
    }
}
