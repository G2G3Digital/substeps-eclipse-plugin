package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.supplier.Transformer;

public class ExternalStepImplementationProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectStepImplementationProvider {

    private final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;
    private final Map<IProject, Set<String>> externalStepClassesForProject;


    public ExternalStepImplementationProvider(
            final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        super();
        this.stepImplementationLoader = stepImplementationLoader;
        this.externalStepClassesForProject = new HashMap<IProject, Set<String>>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);

        workspace.addResourceChangeListener(new IResourceChangeListener() {
            @Override
            public void resourceChanged(final IResourceChangeEvent event) {
                if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                    final Set<IProject> projects = findProjectsInChangeset(event);
                    for (final IProject project : projects) {
                        markAsStale(project);
                    }

                }
            }
        });
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {
        clean(project);
        return externalStepClassesForProject.containsKey(project) ? externalStepClassesForProject.get(project)
                : Collections.<String> emptyList();
    }


    @Override
    protected void clearStepImplementationsFor(final IProject project) {
        super.clearStepImplementationsFor(project);
        this.externalStepClassesForProject.remove(project);
    }


    @Override
    protected Collection<String> findStepImplementationsFor(final IProject project) {

        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationLoader.from(project);
        final Set<String> stepImplementationClasses = new HashSet<String>();

        final Collection<String> suggestions = new ArrayList<String>();
        for (final StepImplementationsDescriptor stepImplementation : stepImplementations) {
            stepImplementationClasses.add(stepImplementation.getClassName());
            for (final StepDescriptor step : stepImplementation.getExpressions()) {
                suggestions.add(step.getExpression());
            }
        }

        externalStepClassesForProject.put(project, stepImplementationClasses);

        return suggestions;
    }


    private Set<IProject> findProjectsInChangeset(final IResourceChangeEvent event) {
        final Set<IProject> changedProjects = new HashSet<IProject>();
        final IResourceDelta[] projectDeltas = event.getDelta().getAffectedChildren();
        for (final IResourceDelta projectDelta : projectDeltas) {
            final IProject project = toProject(projectDelta);
            if (externalStepClassesForProject.containsKey(project)) {
                changedProjects.add(project);
            }
        }
        return changedProjects;
    }


    private IProject toProject(final IResourceDelta projectDelta) {
        final IResource resource = projectDelta.getResource();
        return (IProject) (resource instanceof IProject ? resource : null);
    }
}
