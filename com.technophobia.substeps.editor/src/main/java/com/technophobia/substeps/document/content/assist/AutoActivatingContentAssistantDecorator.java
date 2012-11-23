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
import org.eclipse.jface.text.contentassist.IContentAssistant;

import com.technophobia.substeps.supplier.Callback1;

/**
 * Define default state on a {@link IContentAssistant}
 * 
 * @author sforbes
 * 
 */
public class AutoActivatingContentAssistantDecorator implements Callback1<IContentAssistant> {

    @Override
    public void doCallback(final IContentAssistant input) {
        if (input instanceof ContentAssistant) {
            final ContentAssistant assistant = (ContentAssistant) input;
            assistant.enableAutoActivation(true);
            assistant.setAutoActivationDelay(500);
            assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
            assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
        }
    }

}
