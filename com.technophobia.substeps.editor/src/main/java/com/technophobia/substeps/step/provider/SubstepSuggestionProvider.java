package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.Step;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.step.PatternSuggestion;
import com.technophobia.substeps.step.Suggestion;
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
    protected Collection<Suggestion> findStepImplementationsFor(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<ParentStep> substeps = syntax.getSortedRootSubSteps();
        final List<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (final ParentStep substep : substeps) {
            final Step parentStep = substep.getParent();
            suggestions.add(new PatternSuggestion(parentStep.getPattern(), parentStep.getLine()));
        }
        return suggestions;
    }
}
