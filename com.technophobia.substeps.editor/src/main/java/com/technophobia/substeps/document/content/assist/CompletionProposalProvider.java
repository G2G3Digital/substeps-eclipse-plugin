package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

public interface CompletionProposalProvider {

    ICompletionProposal[] get();
}
