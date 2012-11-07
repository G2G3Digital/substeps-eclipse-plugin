package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepSuggestionProvider extends AbstractMultiProjectSuggestionProvider {

    private final Transformer<IProject, Syntax> projectToSyntaxTransformer;


    public SubstepSuggestionProvider(final Transformer<IProject, Syntax> projectToSyntaxTransformer) {
        this.projectToSyntaxTransformer = projectToSyntaxTransformer;
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);

        workspace.addResourceChangeListener(new FileChangedListener("substeps", new Callback1<IFile>() {

            @Override
            public void doCallback(final IFile file) {
                final IProject project = file.getProject();
                markAsStale(project);
            }
        }));
    }


    @Override
    protected Collection<String> findStepImplementationsFor(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<ParentStep> substeps = syntax.getSortedRootSubSteps();
        final List<String> suggestions = new ArrayList<String>();
        for (final ParentStep substep : substeps) {
            suggestions.add(substep.getParent().getLine());
        }
        return suggestions;
    }
}
