package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;

public abstract class AbstractKeywordContentTypeDefinition extends
		AbstractFeatureContentTypeDefinition {
	
	private final String prefixText;

	public AbstractKeywordContentTypeDefinition(String id, String prefixText,
			boolean optional) {
		super(id, prefixText, optional);
		this.prefixText = prefixText;
	}


    @Override
    public IPredicateRule partitionRule() {
        return singleLineWithTrailingCommentRule(prefixText, id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordRule(prefixText, colourToken(FeatureColour.PINK, colourManager));
    }

}
