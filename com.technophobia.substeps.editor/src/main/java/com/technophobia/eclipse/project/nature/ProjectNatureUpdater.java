package com.technophobia.eclipse.project.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.FeatureEditorPlugin;

public class ProjectNatureUpdater {

    private final String natureId;


    public ProjectNatureUpdater(final String natureId) {
        this.natureId = natureId;
    }


    public void ensureProjectHasNature(final IProject project) {
        final IProjectDescription description = getDescription(project);
        if (description != null) {
            final String[] originalNatureIds = description.getNatureIds();
            if (!isNaturePresent(natureId, originalNatureIds)) {
                description.setNatureIds(addNatureIdTo(natureId, originalNatureIds));
                updateDescription(project, description);
            }

            updatePackageExplorer();
            FeatureEditorPlugin.instance().getProjectObserver().projectChanged(project);
        } else {
            FeatureEditorPlugin.instance().error(
                    "Could not add nature " + natureId + " to current selection, as it is not a valid project");
        }
    }


    public void ensureProjectDoesNotHaveNature(final IProject project) {
        final IProjectDescription description = getDescription(project);
        if (description != null) {
            final String[] originalNatureIds = description.getNatureIds();
            if (isNaturePresent(natureId, originalNatureIds)) {
                description.setNatureIds(removeNatureIdFrom(natureId, originalNatureIds));
                updateDescription(project, description);
            }

            updatePackageExplorer();
            FeatureEditorPlugin.instance().getProjectObserver().projectChanged(project);
        } else {
            FeatureEditorPlugin.instance().error(
                    "Could not remove nature " + natureId + " from current selection, as it is not a valid project");
        }
    }


    private IProjectDescription getDescription(final IProject project) {
        try {
            return project.getDescription();
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not get description for project " + project.getName(), ex);
            return null;
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


    private void updateDescription(final IProject project, final IProjectDescription description) {
        try {
            final NullProgressMonitor monitor = new NullProgressMonitor();
            project.setDescription(description, monitor);
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not update project " + project.getName(), ex);
        }
    }


    private boolean isNaturePresent(final String natureId, final String[] natureIds) {
        for (final String nId : natureIds) {
            if (nId.equals(natureId)) {
                return true;
            }
        }
        return false;
    }


    private String[] addNatureIdTo(final String natureId, final String[] oldNatureIds) {
        final String[] newNatureIds = new String[oldNatureIds.length + 1];
        System.arraycopy(oldNatureIds, 0, newNatureIds, 1, oldNatureIds.length);
        newNatureIds[0] = natureId;
        return newNatureIds;
    }


    private String[] removeNatureIdFrom(final String natureId, final String[] oldNatureIds) {
        final String[] newNatureIds = new String[oldNatureIds.length - 1];
        int i = 0;
        for (final String oldNatureId : oldNatureIds) {
            if (!oldNatureId.equals(natureId)) {
                newNatureIds[i++] = oldNatureId;
            }
        }
        return newNatureIds;
    }
}
