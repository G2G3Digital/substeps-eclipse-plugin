package com.technophobia.substeps.junit.launcher.tab.component;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.technophobia.eclipse.transformer.Callback;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.launcher.DefaultSubstepsLocationFinder;
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModel;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepsFileComponent extends AbstractTabComponent {

    private Text substepsLocationText;
    private Button substepsLocationButton;

    private final Transformer<IProject, String> defaultSubstepsLocationFinder;


    public SubstepsFileComponent(final Callback onChangeCallback, final Supplier<IProject> projectSupplier) {
        this(onChangeCallback, projectSupplier, new DefaultSubstepsLocationFinder());
    }


    public SubstepsFileComponent(final Callback onChangeCallback, final Supplier<IProject> projectSupplier,
            final Transformer<IProject, String> defaultSubstepsLocationFinder) {
        super(onChangeCallback, projectSupplier);
        this.defaultSubstepsLocationFinder = defaultSubstepsLocationFinder;
    }


    @Override
    public void initializeFrom(final SubstepsLaunchModel model) {
        substepsLocationText.setText(model.getSubstepsFile());
    }


    @Override
    public void saveTo(final SubstepsLaunchModel model) {
        if (substepsLocationText != null) {
            model.setSubstepsFile(substepsLocationText.getText().trim());
        }
    }


    @Override
    public void setDefaultOn(final SubstepsLaunchModel model, final IResource currentResource) {
        final String substepsFolder = defaultSubstepsLocationFinder.from(currentResource.getProject());
        if (substepsFolder != null) {
            model.setSubstepsFile(substepsFolder);
        } else {
            model.setSubstepsFile("");
        }
    }


    @Override
    public void create(final Composite comp) {
        final Label substepsLocationLabel = new Label(comp, SWT.NONE);
        substepsLocationLabel.setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_label_substeps_location);
        GridData gd = new GridData();
        gd.horizontalIndent = 25;
        gd.verticalIndent = 5;
        substepsLocationLabel.setLayoutData(gd);

        substepsLocationText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalIndent = 5;
        substepsLocationText.setLayoutData(gd);
        substepsLocationText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent evt) {
                onChange();
            }
        });

        substepsLocationButton = new Button(comp, SWT.PUSH);
        substepsLocationButton.setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_browse_substeps_location);
        substepsLocationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                final String newLocation = handleSubstepsLocationButtonSelected();
                if (newLocation != null && !newLocation.isEmpty()) {
                    substepsLocationText.setText(newLocation);
                }
            }
        });
    }


    @Override
    public void validate(final Collection<String> errorMessageList) {
        final String substepsFileName = substepsLocationText.getText().trim();
        if (substepsFileName.length() == 0) {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_substepsnotdefined);
            return;
        }
        if (!validSubstepsFile(project(), substepsFileName, errorMessageList)) {
            return;
        }
    }


    @Override
    protected boolean isValid() {
        return !substepsLocationText.getText().trim().isEmpty();
    }


    @Override
    public void enableControls() {
        substepsLocationText.setEnabled(true);
        substepsLocationButton.setEnabled(true);
    }


    @Override
    public void disableControls() {
        substepsLocationText.setEnabled(false);
        substepsLocationButton.setEnabled(false);
    }


    /*
     * Show a dialog that lets the user select a folder or substeps file.
     */
    private String handleSubstepsLocationButtonSelected() {
        final IResource resource = chooseSubstepsResource();
        if (resource == null) {
            return "";
        }

        return projectLocalisedPathFor(resource);
    }


    private IResource chooseSubstepsResource() {

        final ILabelProvider labelProvider = new WorkbenchLabelProvider();
        final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(substepsLocationText.getShell(),
                labelProvider, new BaseWorkbenchContentProvider());
        dialog.setTitle(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_substepsdialog_title);
        dialog.setMessage(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_substepsdialog_message);
        dialog.setInput(project());
        dialog.setAllowMultiple(false);
        dialog.setValidator(new ISelectionStatusValidator() {

            @Override
            public IStatus validate(final Object[] selection) {
                if (selection.length > 0) {
                    final Object item = selection[0];
                    if (item instanceof IFile) {
                        final IFile file = (IFile) item;
                        if (!"substeps".equalsIgnoreCase(file.getFileExtension())) {
                            return new Status(IStatus.ERROR, FeatureRunnerPlugin.PLUGIN_ID,
                                    SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_notSubstepsFile);
                        }
                    }
                }
                return new Status(IStatus.OK, FeatureRunnerPlugin.PLUGIN_ID, "");
            }
        });

        if (dialog.open() == Window.OK) {
            return (IResource) dialog.getFirstResult();
        }
        return null;
    }


    /**
     * Determine if a substeps file folder exists under project with name
     * 
     * @param project
     *            The project where substeps lives
     * @param substepsFileName
     *            file/folder name, relative to project
     * @return true if substeps file/folder exists, otherwise false
     */
    private boolean validSubstepsFile(final IProject project, final String substepsFileName,
            final Collection<String> errorMessageList) {
        if (substepsFileName.endsWith(".substeps")) {
            if (!project.getFile(substepsFileName).exists()) {
                errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_substepsnotexists);
                return false;
            }
        } else if (substepsFileName.indexOf('.') > -1) {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_notSubstepsFile);
            return false;
        } else {
            if (!project.getFolder(substepsFileName).exists()) {
                errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_substepsnotexists);
                return false;
            }
        }
        return true;
    }
}
