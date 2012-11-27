/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.syntax;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.model.FeatureFile;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.runner.TagManager;
import com.technophobia.substeps.runner.TestParameters;
import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;
import com.technophobia.substeps.runner.syntax.validation.StepValidator;
import com.technophobia.substeps.runner.syntax.validation.SyntaxAwareStepValidator;

public class ProblemValidatingProjectToSyntaxTransformer extends ProjectToSyntaxTransformer {

    private final PreferenceLookupFactory<IProject> preferenceLookupFactory;
    private final ProjectManager projectManager;


    ProblemValidatingProjectToSyntaxTransformer(final ProjectManager projectManager,
            final PreferenceLookupFactory<IProject> preferenceLookupFactory) {
        // package scope constructor to encourage use of the
        // CachingProjectToSyntaxTransformer
        super(projectManager);
        this.projectManager = projectManager;
        this.preferenceLookupFactory = preferenceLookupFactory;
    }


    @Override
    protected SyntaxErrorReporter syntaxErrorReporterFor(final IProject project) {
        return new MarkerSyntaxErrorReporter(project, preferenceLookupFactory.preferencesFor(project));
    }


    @Override
    protected Syntax buildSyntaxFor(final IProject project, final File substepsFolder,
            final List<Class<?>> stepClasses, final ClassLoader classLoader,
            final SyntaxErrorReporter syntaxErrorReporter) {
        final Syntax syntax = super.buildSyntaxFor(project, substepsFolder, stepClasses, classLoader,
                syntaxErrorReporter);

        // while we're here, lets validate the feature and substep files for
        // missing steps
        reportMissingSteps(project, syntax, syntaxErrorReporter);

        // we know we're using a DeferredReportingSyntaxErrorReporter, so finish
        // it off
        ((DeferredReportingSyntaxErrorReporter) syntaxErrorReporter).applyChanges();

        return syntax;
    }


    private void reportMissingSteps(final IProject project, final Syntax syntax,
            final SyntaxErrorReporter syntaxErrorReporter) {
        final StepValidator validator = new SyntaxAwareStepValidator(syntax);
        reportMissingScenarioSteps(projectManager.featureFolderFor(project).toFile(), syntax, validator,
                syntaxErrorReporter);
        reportMissingSubsteps(syntax.getSubStepsMap().values(), validator, syntaxErrorReporter);
    }


    private void reportMissingScenarioSteps(final File projectFile, final Syntax syntax, final StepValidator validator,
            final SyntaxErrorReporter syntaxErrorReporter) {
        final TestParameters testParameters = new TestParameters(new TagManager(""), syntax,
                projectFile.getAbsolutePath());
        testParameters.init(false);

        for (final FeatureFile featureFile : testParameters.getFeatureFileList()) {
            validator.validateFeatureFile(featureFile, syntaxErrorReporter);
        }
    }


    private void reportMissingSubsteps(final Collection<ParentStep> substeps, final StepValidator validator,
            final SyntaxErrorReporter syntaxErrorReporter) {
        for (final ParentStep substep : substeps) {
            validator.validateSubstep(substep, syntaxErrorReporter);
        }
    }
}
