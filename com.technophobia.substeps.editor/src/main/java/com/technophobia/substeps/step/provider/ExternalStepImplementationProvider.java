package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.supplier.Transformer;

public class ExternalStepImplementationProvider extends AbstractMultiProjectSuggestionProvider {

    private final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;


    public ExternalStepImplementationProvider(
            final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        super();
        this.stepImplementationLoader = stepImplementationLoader;
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);

        workspace.addResourceChangeListener(new AddRemoveExternalStepImplementationsListener(this));
    }


    @Override
    protected Collection<String> findStepImplementationsFor(final IProject project) {

        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationLoader.to(project);

        final Collection<String> suggestions = new ArrayList<String>();
        for (final StepImplementationsDescriptor stepImplementation : stepImplementations) {
            for (final StepDescriptor step : stepImplementation.getExpressions()) {
                suggestions.add(step.getExpression());
            }
        }
        return suggestions;
    }
}
