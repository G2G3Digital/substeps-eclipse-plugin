package com.technophobia.substeps.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.nature.SubstepsNature;

public abstract class AbstractSubstepsNatureAction implements IObjectActionDelegate {

    private IProject currentProject = null;


    @Override
    public void run(final IAction action) {
        if (currentProject != null) {
            final IProjectDescription description = getDescription(currentProject);
            if (description != null) {
                final String[] originalNatureIds = description.getNatureIds();
                description.setNatureIds(getUpdatedNatureIds(SubstepsNature.NATURE_ID, originalNatureIds,
                        isNaturePresent(SubstepsNature.NATURE_ID, originalNatureIds)));
                updateDescription(currentProject, description);

                FeatureEditorPlugin.instance().getProjectObserver().projectChanged(currentProject);
            }
        } else {
            FeatureEditorPlugin.instance().error(
                    "Could not add substeps nature to current selection, as it is not a valid project");
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


    protected abstract String[] getUpdatedNatureIds(String natureId, String[] originalNatureIds, boolean naturePresent);


    private boolean isNaturePresent(final String natureId, final String[] natureIds) {
        for (final String nId : natureIds) {
            if (nId.equals(natureId)) {
                return true;
            }
        }
        return false;
    }


    private IProjectDescription getDescription(final IProject project) {
        try {
            return project.getDescription();
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not get description for project " + project.getName(), ex);
            return null;
        }
    }


    private void updateDescription(final IProject project, final IProjectDescription description) {
        try {
            final NullProgressMonitor monitor = new NullProgressMonitor();
            currentProject.setDescription(description, monitor);
            updatePackageExplorer();
            // currentProject.refreshLocal(1, monitor);
            // currentProject.touch(monitor);
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not update project " + project.getName(), ex);
        }
    }


    private void updatePackageExplorer() {
        final IViewPart foundView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .findView(JavaUI.ID_PACKAGES);

        if (foundView instanceof IPackagesViewPart) {
            final IPackagesViewPart packageExplorerView = (IPackagesViewPart) foundView;
            final TreeViewer treeViewer = packageExplorerView.getTreeViewer();
            treeViewer.refresh();
        }

    }
}
