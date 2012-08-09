package com.technophobia.substeps.junit.launcher.tab;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.transformer.Callback;
import com.technophobia.substeps.junit.launcher.DefaultSubstepsLocationFinder;
import com.technophobia.substeps.junit.launcher.model.LaunchModelFactory;
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModel;
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModelFactory;
import com.technophobia.substeps.junit.launcher.tab.component.FeatureFileComponent;
import com.technophobia.substeps.junit.launcher.tab.component.ProjectComponent;
import com.technophobia.substeps.junit.launcher.tab.component.SubstepsFileComponent;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class SubstepsArgumentTab extends AbstractLaunchConfigurationTab {

    private final TabComponent projectComponent;
    private final TabComponent featureFileComponent;
    private final TabComponent substepsComponent;

    private final LaunchModelFactory launchModelFactory;


    public SubstepsArgumentTab() {
        this.launchModelFactory = new SubstepsLaunchModelFactory(new DefaultSubstepsLocationFinder());

        final Callback onChangeCallback = onChange();
        final ProjectComponent pc = new ProjectComponent(onChangeCallback);

        this.projectComponent = pc;
        this.featureFileComponent = new FeatureFileComponent(onChangeCallback, pc);
        this.substepsComponent = new SubstepsFileComponent(onChangeCallback, pc);

        projectComponent.addDependentTabComponent(featureFileComponent);
        projectComponent.addDependentTabComponent(substepsComponent);
    }


    @Override
    public void createControl(final Composite parent) {
        final Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), getHelpContextId());
        final GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        comp.setLayout(layout);
        comp.setFont(parent.getFont());

        projectComponent.create(comp);
        featureFileComponent.create(comp);
        substepsComponent.create(comp);
    }


    @Override
    public void initializeFrom(final ILaunchConfiguration config) {
        final SubstepsLaunchModel model = (SubstepsLaunchModel) launchModelFactory.createFrom(config);
        projectComponent.initializeFrom(model);
        featureFileComponent.initializeFrom(model);
        substepsComponent.initializeFrom(model);

        validatePage();
    }


    @Override
    public String getName() {
        return SubstepsFeatureMessages.SubstepsLaunchConfigurationTab_tab_label;
    }


    @Override
    public void performApply(final ILaunchConfigurationWorkingCopy config) {
        final SubstepsLaunchModel launchModel = new SubstepsLaunchModel();
        projectComponent.saveTo(launchModel);
        featureFileComponent.saveTo(launchModel);
        substepsComponent.saveTo(launchModel);

        launchModel.saveTo(config);
    }


    @Override
    public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
        final IResource currentResource = getContext();

        final SubstepsLaunchModel launchModel = new SubstepsLaunchModel();

        if (currentResource != null) {
            configuration.rename(currentResource.getName());
            projectComponent.setDefaultOn(launchModel, currentResource);
            featureFileComponent.setDefaultOn(launchModel, currentResource);
            substepsComponent.setDefaultOn(launchModel, currentResource);
        }
        launchModel.saveTo(configuration);
    }


    private Callback onChange() {
        return new Callback() {

            @Override
            public void callback() {
                final boolean result = validatePage();
                if (result) {
                    updateLaunchConfigurationDialog();
                }
            }
        };
    }


    private boolean validatePage() {

        setErrorMessage(null);
        setMessage(null);

        final Collection<String> errorMessages = new ArrayList<String>();
        projectComponent.validate(errorMessages);
        if (errorMessages.isEmpty()) {
            featureFileComponent.validate(errorMessages);
            substepsComponent.validate(errorMessages);
        }

        if (!errorMessages.isEmpty()) {
            setErrorMessage(errorMessages.iterator().next());
            return false;
        }
        return true;
    }


    /*
     * Returns the current resource element context from which to initialize
     * default settings, or <code>null</code> if none.
     * 
     * @return resource context.
     */
    private IResource getContext() {
        final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return null;
        }
        final IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page != null) {
            final ISelection selection = page.getSelection();
            if (selection instanceof IStructuredSelection) {
                final IStructuredSelection ss = (IStructuredSelection) selection;
                if (!ss.isEmpty()) {
                    final Object obj = ss.getFirstElement();

                    if (obj instanceof IResource) {
                        return (IResource) obj;
                    }
                }
            }
            final IEditorPart part = page.getActiveEditor();
            if (part != null) {
                final IEditorInput input = part.getEditorInput();
                return (IResource) input.getAdapter(IResource.class);
            }
        }
        return null;
    }

}
