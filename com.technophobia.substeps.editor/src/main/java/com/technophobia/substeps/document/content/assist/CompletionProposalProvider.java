package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * Provides all {@link ICompletionProposal}s for a given document context
 * 
 * @author sforbes
 * 
 */
public interface CompletionProposalProvider {

    /**
     * Return all {@link ICompletionProposal}s for the document at the offset
     * 
     * @param document
     *            The currently edited document
     * @param offset
     *            Where in the document the cursor is currently located
     * @return All relevant completion proposals
     */
    ICompletionProposal[] get(IDocument document, int offset);
}
