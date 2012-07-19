package com.technophobia.substeps.document.content.assist.feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import com.technophobia.substeps.document.content.assist.CompletionProposalProvider;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.runner.syntax.SyntaxBuilder;

public class StepImplementationProposalProvider implements CompletionProposalProvider {

    private final String outputFolder;
    private final ClassLocator classLocator;


    public StepImplementationProposalProvider(final String outputFolder, final ClassLocator classLocator) {
        this.outputFolder = outputFolder;
        this.classLocator = classLocator;
    }


    @Override
    public ICompletionProposal[] get() {
        final List<Class<?>> stepClasses = stepClasses();

        final Syntax syntax = SyntaxBuilder.buildSyntax(stepClasses, null);

        for (final StepImplementation step : syntax.getStepImplementations()) {
            System.out.println("step: " + step);
        }

        return new ICompletionProposal[0];
    }


    private List<Class<?>> stepClasses() {
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
