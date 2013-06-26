package com.technophobia.substeps.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractSubstepsNatureAction implements IObjectActionDelegate {

    private IProject currentProject = null;


    @Override
    public void run(final IAction action) {
        if (currentProject != null) {
            updateProject(currentProject);
        }
    }


    @Override
    public void selectionChanged(final IAction action, final ISelection selection) {
        boolean updatedCurrentProject = false;
        if (selection instanceof IStructuredSelection) {
            final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.getFirstElement() instanceof IProject) {
                currentProject = (IProject) structuredSelection.getFirstElement();
                updatedCurrentProject = true;
            }
        }

        if (!updatedCurrentProject) {
            currentProject = null;
        }
    }


    @Override
    public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
        // No-op
    }


    protected abstract void updateProject(IProject project);
}
