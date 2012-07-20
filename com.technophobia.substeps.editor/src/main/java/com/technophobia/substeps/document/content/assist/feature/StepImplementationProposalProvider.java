package com.technophobia.substeps.document.content.assist.feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.eclipse.transformer.SiteToJavaProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.classloader.ClassLoadedClassAnalyser;
import com.technophobia.substeps.classloader.JavaProjectClassLoader;
import com.technophobia.substeps.document.content.assist.CompletionProposalProvider;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.runner.runtime.StepClassLocator;
import com.technophobia.substeps.runner.syntax.SyntaxBuilder;

public class StepImplementationProposalProvider implements CompletionProposalProvider {

    private final IWorkbenchPartSite site;


    public StepImplementationProposalProvider(final IWorkbenchPartSite site) {
        this.site = site;
    }


    @Override
    public ICompletionProposal[] get() {
        final IJavaProject project = new SiteToJavaProjectTransformer().to(site);
        final JavaProjectClassLoader classLoader = new JavaProjectClassLoader(project);
        final String outputFolder = outputFolderForProject(project);
        final ClassLocator classLocator = new StepClassLocator(outputFolder, classLoader);

        final List<Class<?>> stepClasses = stepClasses(outputFolder, classLocator);

        final Syntax syntax = SyntaxBuilder.buildSyntax(stepClasses, null, true, null, new ClassLoadedClassAnalyser(
                classLoader));

        for (final StepImplementation step : syntax.getStepImplementations()) {
            System.out.println("step: " + step.getValue());
        }

        return new ICompletionProposal[0];
    }


    private String outputFolderForProject(final IJavaProject project) {
        try {
            final IPath projectLocation = project.getResource().getLocation().makeAbsolute();
            final IPath outputLocation = project.getOutputLocation();
            return projectLocation.append(outputLocation.removeFirstSegments(1)).toOSString();
        } catch (final JavaModelException e) {
            FeatureEditorPlugin.log(IStatus.ERROR, "Could not get output folder for project " + project);
        }
        return null;
    }


    private List<Class<?>> stepClasses(final String outputFolder, final ClassLocator classLocator) {
        return toList(classLocator.fromPath(outputFolder));
    }


    private List<Class<?>> toList(final Iterator<Class<?>> it) {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

}
