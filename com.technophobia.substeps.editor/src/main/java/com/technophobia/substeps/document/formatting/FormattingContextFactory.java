package com.technophobia.substeps.document.formatting;

import org.eclipse.jface.text.TypedPosition;

/**
 * Create a new {@link FormattingContext} relative to the current position
 * 
 * @author sforbes
 * 
 */
public interface FormattingContextFactory {

    /**
     * Create a {@link FormattingContext} for the position
     * 
     * @param position
     * @param currentPosition
     * @return
     */
    FormattingContext createFor(TypedPosition[] position, int currentPosition);
}