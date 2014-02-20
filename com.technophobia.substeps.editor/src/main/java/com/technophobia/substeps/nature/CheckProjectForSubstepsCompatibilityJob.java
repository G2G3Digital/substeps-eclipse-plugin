package com.technophobia.substeps.nature;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;

import com.technophobia.substeps.editor.message.SubstepsEditorMessages;
import com.technophobia.substeps.supplier.Transformer;

public class CheckProjectForSubstepsCompatibilityJob extends Job {

    private final Transformer<IProject, IPersistentPreferenceStore> projectToPreferenceLookup;
    private final IWorkbench workbench;


    public CheckProjectForSubstepsCompatibilityJob(final IWorkbench workbench,
            final Transformer<IProject, IPersistentPreferenceStore> projectToPreferenceLookup) {
        super("Checking Projects for Substeps compatibility");
        this.workbench = workbench;
        this.projectToPreferenceLookup = projectToPreferenceLookup;
    }


    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final SubstepsCompatibilityChecker compatibilityChecker = new SubstepsCompatibilityChecker(
                projectToPreferenceLookup);

        final Collection<IProject> allProjects = allOpenProjects();
        for (final IProject project : allProjects) {
            if (compatibilityChecker.isCandidateForAddingNature(project)) {
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (MessageDialog.openQuestion(
                                workbench.getDisplay().getActiveShell(),
                                SubstepsEditorMessages.SubstepsProjectCompatibility_Title,
                                MessageFormat.format(SubstepsEditorMessages.SubstepsProjectCompatibility_Body,
                                        project.getName()))) {

                            SubstepsNature.ensureProjectHasNature(project);
                        }

                        compatibilityChecker.markProjectAsCompatibilityChecked(project);
                    }
                });
            }
        }

        return Status.OK_STATUS;
    }


    private Collection<IProject> allOpenProjects() {
        final Collection<IProject> openProjects = new ArrayList<IProject>();
        final IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (final IProject project : allProjects) {
            if (project.isOpen()) {
                openProjects.add(project);
            }
        }
        return Collections.unmodifiableCollection(openProjects);
    }
}
