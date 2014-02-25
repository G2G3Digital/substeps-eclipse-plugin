package com.technophobia.substeps.nature;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;

import com.technophobia.substeps.editor.message.SubstepsEditorMessages;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.supplier.CachingResultTransformer;
import com.technophobia.substeps.supplier.Supplier;

public class CheckProjectForSubstepsCompatibilityJob extends Job {

    private final Supplier<IWorkbench> workbenchSupplier;
    private final CompatibilityChecker<IProject> compatibilityChecker;
    private final CachingResultTransformer<IProject, Syntax> projectToSyntaxTransformer;
    private final Supplier<List<IProject>> projectSupplier;


    public CheckProjectForSubstepsCompatibilityJob(final Supplier<IWorkbench> workbenchSupplier,
            final Supplier<List<IProject>> projectSupplier, final CompatibilityChecker<IProject> compatibilityChecker,
            final CachingResultTransformer<IProject, Syntax> projectToSyntaxTransformer) {
        super("Checking Projects for Substeps compatibility");
        this.workbenchSupplier = workbenchSupplier;
        this.projectSupplier = projectSupplier;
        this.compatibilityChecker = compatibilityChecker;
        this.projectToSyntaxTransformer = projectToSyntaxTransformer;
    }


    @Override
    protected IStatus run(final IProgressMonitor monitor) {

        for (final IProject project : projectSupplier.get()) {
            if (compatibilityChecker.isCompatible(project)) {
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (MessageDialog.openQuestion(
                                workbenchSupplier.get().getDisplay().getActiveShell(),
                                SubstepsEditorMessages.SubstepsProjectCompatibility_Title,
                                MessageFormat.format(SubstepsEditorMessages.SubstepsProjectCompatibility_Body,
                                        project.getName()))) {

                            SubstepsNature.ensureProjectHasNature(project);
                            projectToSyntaxTransformer.refreshCacheFor(project);
                        }

                        compatibilityChecker.markResourceAsCompatibilityChecked(project);
                    }
                });
            }
        }

        return Status.OK_STATUS;
    }
}
