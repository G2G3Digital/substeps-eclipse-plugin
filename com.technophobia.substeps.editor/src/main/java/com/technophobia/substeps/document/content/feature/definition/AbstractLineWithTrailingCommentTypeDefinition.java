package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;

public abstract class AbstractLineWithTrailingCommentTypeDefinition extends
		AbstractFeatureContentTypeDefinition {

	private final String prefixText;
	private final FeatureColour featureColour;

	public AbstractLineWithTrailingCommentTypeDefinition(String id,
			String prefixText, boolean optional, FeatureColour featureColour) {
		super(id, prefixText, optional);
		this.prefixText = prefixText;
		this.featureColour = featureColour;
	}


    @Override
    public IPredicateRule partitionRule() {
        return singleLineWithTrailingCommentRule(prefixText, id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordRule(prefixText, colourToken(featureColour, colourManager));
    }
}
