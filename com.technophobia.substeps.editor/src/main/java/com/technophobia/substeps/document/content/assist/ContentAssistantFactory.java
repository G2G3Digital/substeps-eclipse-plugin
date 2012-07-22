package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.contentassist.IContentAssistant;

/**
 * Factory for {@link IContentAssistant}
 * 
 * @author sforbes
 * 
 */
public interface ContentAssistantFactory {

    /**
     * Create a new {@link IContentAssistant}
     * 
     * @return the new content assistant
     */
    IContentAssistant createContentAssist();
}
