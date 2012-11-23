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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class NoCompletionsProposal implements ICompletionProposal {

    @Override
    public void apply(final IDocument document) {
        // no-op
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
