package com.technophobia.substeps.junit.launcher.tab.component;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
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
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.technophobia.eclipse.transformer.Callback;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModel;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.supplier.Supplier;

public class ProjectComponent extends AbstractTabComponent implements Supplier<IProject> {

    private Text projectText = null;


    public ProjectComponent(final Callback onChangeCallback) {
        super(onChangeCallback, null);
    }


    @Override
    public void initializeFrom(final SubstepsLaunchModel model) {
        projectText.setText(model.getProjectName());
    }


    @Override
    public void saveTo(final SubstepsLaunchModel model) {
        if (projectText != null) {
            model.setProjectName(projectText.getText().trim());
        }
    }


    @Override
    public void setDefaultOn(final SubstepsLaunchModel model, final IResource currentResource) {
        final IProject project = currentResource.getProject();
        model.setProjectName(project.getName());
    }


    @Override
    public void validate(final Collection<String> errorMessageList) {
        final String projectName = projectText.getText().trim();
        if (projectName.length() == 0) {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_projectnotdefined);
        }

        final IStatus status = ResourcesPlugin.getWorkspace().validatePath(IPath.SEPARATOR + projectName,
                IResource.PROJECT);
        if (!status.isOK() || !Path.ROOT.isValidSegment(projectName)) {
            errorMessageList.add(MessageFormat.format(
                    SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_invalidProjectName, projectName));
            return;
        }

        final IProject project = getWorkspaceRoot().getProject(projectName);
        if (!project.exists()) {
            errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_projectnotexists);
            return;
        }

        try {
            if (!project.hasNature(JavaCore.NATURE_ID)) {
                errorMessageList.add(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_error_notJavaProject);
                return;
            }
        } catch (final CoreException e) {
            FeatureRunnerPlugin.log(e);
        }
    }


    @Override
    public void create(final Composite comp) {
        final Label projectLabel = new Label(comp, SWT.NONE);
        projectLabel.setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_label_project_location);
        GridData gd = new GridData();
        gd.horizontalIndent = 25;
        gd.verticalIndent = 5;
        projectLabel.setLayoutData(gd);

        projectText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalIndent = 5;
        projectText.setLayoutData(gd);
        projectText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent evt) {
                onChange();
            }
        });

        final Button projectButton = new Button(comp, SWT.PUSH);
        projectButton.setText(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_browse_project_location);
        projectButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                final String projectName = handleProjectButtonSelected();
                if (projectName != null && !projectName.isEmpty()) {
                    projectText.setText(projectName);
                }
            }
        });
    }


    @Override
    protected boolean isValid() {
        return !projectText.getText().trim().isEmpty();
    }


    @Override
    public void enableControls() {
        projectText.setEnabled(true);
    }


    @Override
    public void disableControls() {
        projectText.setEnabled(false);
    }


    @Override
    public IProject get() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectText.getText().trim());
    }


    /*
     * Show a dialog that lets the user select a folder or substeps file.
     */
    private String handleProjectButtonSelected() {
        final IJavaProject project = chooseJavaProject();
        if (project == null) {
            return "";
        }

        return project.getElementName();
    }


    /*
     * Realize a Java Project selection dialog and return the first selected
     * project, or null if there was none.
     */
    private IJavaProject chooseJavaProject() {
        IJavaProject[] projects;
        try {
            projects = JavaCore.create(getWorkspaceRoot()).getJavaProjects();
        } catch (final JavaModelException e) {
            FeatureRunnerPlugin.log(e);
            projects = new IJavaProject[0];
        }

        final ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        final ElementListSelectionDialog dialog = new ElementListSelectionDialog(projectText.getShell(), labelProvider);
        dialog.setTitle(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_projectdialog_title);
        dialog.setMessage(SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_projectdialog_message);
        dialog.setElements(projects);

        final IJavaProject javaProject = getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[] { javaProject });
        }
        if (dialog.open() == Window.OK) {
            return (IJavaProject) dialog.getFirstResult();
        }
        return null;
    }


    /*
     * Return the IJavaProject corresponding to the project name in the project
     * name text field, or null if the text does not match a project name.
     */
    private IJavaProject getJavaProject() {
        final String projectName = projectText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return getJavaModel().getJavaProject(projectName);
    }


    /*
     * Convenience method to get access to the java model.
     */
    private IJavaModel getJavaModel() {
        return JavaCore.create(getWorkspaceRoot());
    }
}
