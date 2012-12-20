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
package com.technophobia.substeps.document.content.assist.feature;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.substeps.document.content.assist.CompletionProvidedContentProcessor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Supplies {@link IContentAssistProcessor} based on supplied
 * {@link StepImplementation} that it looks up on the classpath
 * 
 * @author sforbes
 * 
 */
public class StepImplementationProcessorSupplier implements Supplier<IContentAssistProcessor> {

    private final IWorkbenchPartSite site;
    private final ContextualSuggestionManager suggestionManager;


    public StepImplementationProcessorSupplier(final IWorkbenchPartSite site,
            final ContextualSuggestionManager suggestionManager) {
        this.site = site;
        this.suggestionManager = suggestionManager;
    }


    @Override
    public IContentAssistProcessor get() {
        return new CompletionProvidedContentProcessor(new StepImplementationProposalProvider(site, suggestionManager));
    }

}
