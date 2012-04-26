package com.technophobia.substeps.document.content;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;

public class SingleRuleBasedScannerElementScanner extends RuleBasedScanner {

	public SingleRuleBasedScannerElementScanner(final IRule rule) {
		final IRule[] rules = new IRule[1];

		rules[0] = rule;

		setRules(rules);
	}

}
