package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public interface CompletionProposalProvider {

    ICompletionProposal[] get(IDocument document, int offset);
}
