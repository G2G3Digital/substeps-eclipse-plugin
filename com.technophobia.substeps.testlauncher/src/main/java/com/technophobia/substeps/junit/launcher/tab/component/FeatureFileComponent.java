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
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModel;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.supplier.Supplier;

public class FeatureFileComponent extends AbstractTabComponent {

    private Text featureFileLocationText;
    private Button featureFileLocationButton;


    public FeatureFileComponent(final Callback onChangeCallback, final Supplier<IProject> projectSupplier) {
        super(onChangeCallback, projectSupplier);
    }


    @Override
    public void initializeFrom(final SubstepsLaunchModel model) {
        featureFileLocationText.setText(model.getFeatureFile());
    }


    @Override
    public void saveTo(final SubstepsLaunchModel model) {
        if (featureFileLocationText != null) {
            model.setFeatureFile(featureFileLocationText.getText().trim());
        }
    }


    @Override
    public void setDefaultOn(final SubstepsLaunchModel model, final IResource currentResource) {
        if (currentResource instanceof IFile) {
            final IFile file = (IFile) currentResource;
            if (file.getFileExtension().equalsIgnoreCase("feature")) {
                model.setFeatureFile(projectLocalisedPathFor(file));
            }
        }
    }


    @Override
    public void validate(final Collection<String> errorMessageList) {
        final String featureFileName = featureFileLocationText.getText().trim();
        if (featureFileName.length() == 0) {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_featurefilenotdefined);
            return;
        }
        if (!validFeatureFile(project(), featureFileName, errorMessageList)) {
            return;
        }
    }


    @Override
    public void create(final Composite comp) {
        final Label featureFileLocationLabel = new Label(comp, SWT.NONE);
        featureFileLocationLabel.setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_label_feature_location);
        GridData gd = new GridData();
        gd.horizontalIndent = 25;
        gd.verticalIndent = 5;
        featureFileLocationLabel.setLayoutData(gd);

        featureFileLocationText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalIndent = 5;
        featureFileLocationText.setLayoutData(gd);
        featureFileLocationText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent evt) {
                onChange();
            }
        });

        featureFileLocationButton = new Button(comp, SWT.PUSH);
        featureFileLocationButton
                .setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_browse_feature_location);
        featureFileLocationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                final String newLocation = handleFeatureFileLocationButtonSelected();
                if (newLocation != null && !newLocation.isEmpty()) {
                    featureFileLocationText.setText(newLocation);
                }
            }
        });
    }


    @Override
    protected boolean isValid() {
        return !featureFileLocationText.getText().trim().isEmpty();
    }


    @Override
    public void enableControls() {
        featureFileLocationText.setEnabled(true);
        featureFileLocationButton.setEnabled(true);
    }


    @Override
    public void disableControls() {
        featureFileLocationText.setEnabled(false);
        featureFileLocationButton.setEnabled(false);
    }


    /*
     * Show a dialog that lets the user select a folder or substeps file.
     */
    private String handleFeatureFileLocationButtonSelected() {
        final IResource resource = chooseFeatureFileResource();
        if (resource == null) {
            return "";
        }

        return projectLocalisedPathFor(resource);
    }


    private IResource chooseFeatureFileResource() {

        final ILabelProvider labelProvider = new WorkbenchLabelProvider();
        final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(featureFileLocationText.getShell(),
                labelProvider, new BaseWorkbenchContentProvider());
        dialog.setTitle(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_featurefiledialog_title);
        dialog.setMessage(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_featurefiledialog_message);
        dialog.setInput(project());
        dialog.setAllowMultiple(false);
        dialog.setValidator(new ISelectionStatusValidator() {

            @Override
            public IStatus validate(final Object[] selection) {
                if (selection.length > 0) {
                    final Object item = selection[0];
                    if (item instanceof IFile) {
                        final IFile file = (IFile) item;
                        if ("feature".equalsIgnoreCase(file.getFileExtension())) {
                            return new Status(IStatus.OK, FeatureRunnerPlugin.PLUGIN_ID, "");
                        }
                    }
                }
                return new Status(IStatus.ERROR, FeatureRunnerPlugin.PLUGIN_ID,
                        SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_notFeatureFile);
            }
        });

        if (dialog.open() == Window.OK) {
            return (IResource) dialog.getFirstResult();
        }
        return null;
    }


    /**
     * Determine if a feature file exists under project with name
     * 
     * @param project
     *            The project where substeps lives
     * @param featureFileName
     *            file name, relative to project
     * @return true if feature file exists, otherwise false
     */
    private boolean validFeatureFile(final IProject project, final String featureFileName,
            final Collection<String> errorMessageList) {
        if (featureFileName.endsWith(".feature")) {
            if (!project.getFile(featureFileName).exists()) {
                errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_featurefilenotexists);
                return false;
            }
        } else {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_notFeatureFile);
            return false;
        }
        return true;
    }
}
