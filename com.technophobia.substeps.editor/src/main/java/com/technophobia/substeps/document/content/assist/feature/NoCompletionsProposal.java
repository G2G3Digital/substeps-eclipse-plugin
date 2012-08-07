package com.technophobia.substeps.document.content.assist.feature;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class NoCompletionsProposal implements ICompletionProposal {

    @Override
    public void apply(final IDocument document) {
    }


    @Override
    public Point getSelection(final IDocument document) {
        return null;
    }


    @Override
    public String getAdditionalProposalInfo() {
        return null;
    }


    @Override
    public String getDisplayString() {
        return "No suggestions available";
    }


    @Override
    public Image getImage() {
        return null;
    }


    @Override
    public IContextInformation getContextInformation() {
        return null;
    }

}
