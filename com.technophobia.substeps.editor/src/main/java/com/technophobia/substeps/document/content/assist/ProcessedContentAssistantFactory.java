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
package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;

import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Implementation of {@link ContentAssistantFactory} that adds a supplied
 * {@link IContentAssistProcessor} to a new {@link IContentAssistant}, followed
 * by decorating it using {@link Callback1}'s
 * 
 * @author sforbes
 * 
 */
public class ProcessedContentAssistantFactory implements ContentAssistantFactory {

    private static final String DEFAULT_PARTITION_CONTENT_TYPE = "__dftl_partition_content_type";
    private final Supplier<IContentAssistProcessor> processorSupplier;
    private final Callback1<IContentAssistant>[] contentAssistantDecorators;


    public ProcessedContentAssistantFactory(final Supplier<IContentAssistProcessor> processorSupplier,
            final Callback1<IContentAssistant>... contentAssistantDecorators) {
        this.processorSupplier = processorSupplier;
        this.contentAssistantDecorators = contentAssistantDecorators;
    }


    @Override
    public IContentAssistant createContentAssist() {

        final ContentAssistant assistant = new ContentAssistant();
        final IContentAssistProcessor processor = processorSupplier.get();

        assistant.setContentAssistProcessor(processor, StepContentTypeDefinition.CONTENT_TYPE_ID);
        assistant.setContentAssistProcessor(processor, DefineContentTypeDefinition.CONTENT_TYPE_ID);
        assistant.setContentAssistProcessor(processor, DEFAULT_PARTITION_CONTENT_TYPE);

        for (final Callback1<IContentAssistant> callback : contentAssistantDecorators) {
            callback.doCallback(assistant);
        }

        return assistant;
    }

}
