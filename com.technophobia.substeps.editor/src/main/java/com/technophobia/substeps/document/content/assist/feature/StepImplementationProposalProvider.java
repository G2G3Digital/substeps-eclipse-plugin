package com.technophobia.substeps.document.content.assist.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.eclipse.transformer.SiteToJavaProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.classloader.ClassLoadedClassAnalyser;
import com.technophobia.substeps.classloader.JavaProjectClassLoader;
import com.technophobia.substeps.document.content.assist.CompletionProposalProvider;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.render.StepImplementationRenderer;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.runner.runtime.StepClassLocator;
import com.technophobia.substeps.runner.syntax.SyntaxBuilder;

public class StepImplementationProposalProvider implements CompletionProposalProvider {

    private final IWorkbenchPartSite site;
    private final StepImplementationRenderer stepRenderer;


    public StepImplementationProposalProvider(final IWorkbenchPartSite site,
            final StepImplementationRenderer stepRenderer) {
        this.site = site;
        this.stepRenderer = stepRenderer;
    }


    @Override
    public ICompletionProposal[] get(final IDocument document, final int offset) {
        final IJavaProject project = new SiteToJavaProjectTransformer().to(site);
        final JavaProjectClassLoader classLoader = new JavaProjectClassLoader(project);
        final String outputFolder = outputFolderForProject(project);
        final ClassLocator classLocator = new StepClassLocator(outputFolder, classLoader);

        final List<Class<?>> stepClasses = stepClasses(outputFolder, classLocator);

        final Syntax syntax = SyntaxBuilder.buildSyntax(stepClasses, null, true, null, new ClassLoadedClassAnalyser(
                classLoader));

        final Collection<String> sortedSuggestions = getSuggestionsForSteps(syntax.getStepImplementations());
        return createCompletionsForSuggestions(document, offset, sortedSuggestions);
    }


    private List<String> getSuggestionsForSteps(final List<StepImplementation> steps) {
        final List<String> suggestions = new ArrayList<String>();
        for (final StepImplementation step : steps) {
            final String stepText = stepRenderer.render(step);
            suggestions.add(stepText);
        }
        Collections.sort(suggestions);
        return suggestions;
    }


    private ICompletionProposal[] createCompletionsForSuggestions(final IDocument document, final int offset,
            final Collection<String> suggestions) {

        final Collection<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();

        // filter the list based on current text
        final String startOfLastWord = getLastWord(document, offset);

        for (final String suggestion : suggestions) {
            // TODO - position the cursor at the first < in order to be able
            // to replace with something sensible

            if (startOfLastWord == null) {
                completionProposals.add(new CompletionProposal(suggestion, offset, 0, suggestion.length()));
            } else {
                // only include if the suggestion matches
                if (suggestion.toUpperCase().startsWith(startOfLastWord.toUpperCase())) {
                    // String actualReplacement =
                    // replacement.substring(startOfLastWord.length());
                    // result.add(
                    // new CompletionProposal(actualReplacement, offset, 0,
                    // actualReplacement.length()));

                    completionProposals.add(new CompletionProposal(suggestion, offset - startOfLastWord.length(),
                            startOfLastWord.length(), suggestion.length()));

                }
            }
        }

        return completionProposals.toArray(new ICompletionProposal[completionProposals.size()]);
    }


    private String getLastWord(final IDocument document, final int offset) {
        try {
            final int lineNumber = document.getLineOfOffset(offset);
            final int lineStart = document.getLineOffset(lineNumber);

            final String line = document.get(lineStart, offset - lineStart);
            return line.trim();

        } catch (final BadLocationException e) {
            FeatureEditorPlugin.log(IStatus.ERROR, "Could not get last word");
            // ... log the exception ...
        }
        return null;
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
