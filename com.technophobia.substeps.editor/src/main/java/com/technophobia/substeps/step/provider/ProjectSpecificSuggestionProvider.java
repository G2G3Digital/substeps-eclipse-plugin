package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.render.StepImplementationRenderer;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectSpecificSuggestionProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectStepImplementationProvider {

    private final Transformer<IProject, Syntax> projectToSyntaxTransformer;
    private final StepImplementationRenderer stepRenderer;

    private final Set<IProject> staleProjects;


    public ProjectSpecificSuggestionProvider(final Transformer<IProject, Syntax> projectToSyntaxTransformer,
            final StepImplementationRenderer stepRenderer) {
        super();
        this.projectToSyntaxTransformer = projectToSyntaxTransformer;
        this.stepRenderer = stepRenderer;
        this.staleProjects = new HashSet<IProject>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);

        JavaCore.addElementChangedListener(new StepImplementationClassChangedListener(new Callback1<IProject>() {
            @Override
            public void doCallback(final IProject project) {
                clearStepImplementationsFor(project);
                staleProjects.add(project);
            }
        }));
    }


    @Override
    public Collection<String> suggestionsFor(final IProject project) {
        if (staleProjects.contains(project)) {
            loadProject(project);
            staleProjects.remove(project);
        }
        return super.suggestionsFor(project);
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<StepImplementation> stepImplementations = syntax.getStepImplementations();

        final Collection<String> classes = new HashSet<String>();
        for (final StepImplementation stepImplementation : stepImplementations) {
            classes.add(stepImplementation.getImplementedIn().getName());
        }
        return classes;
    }


    @Override
    protected Collection<String> findStepImplementationsFor(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<StepImplementation> stepImplementations = syntax.getStepImplementations();
        final Collection<String> suggestions = new ArrayList<String>(stepImplementations.size());
        for (final StepImplementation stepImplementation : stepImplementations) {
            suggestions.add(stepRenderer.render(stepImplementation));
        }

        return suggestions;
    }
}
