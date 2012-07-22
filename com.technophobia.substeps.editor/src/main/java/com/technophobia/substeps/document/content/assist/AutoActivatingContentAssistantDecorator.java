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
