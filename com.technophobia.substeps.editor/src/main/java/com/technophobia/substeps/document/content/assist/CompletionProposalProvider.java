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
