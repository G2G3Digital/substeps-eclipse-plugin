package com.technophobia.substeps.document.content.feature;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.FixedIndentFormattingStrategy;
import com.technophobia.substeps.document.formatting.strategy.OptionalUnitPrefixFormattingStrategy;
import com.technophobia.substeps.document.formatting.strategy.StartOfUnitFormattingStrategy;
import com.technophobia.substeps.supplier.Supplier;

public enum FeatureContentTypeDefinition implements ContentTypeDefinition {

	FEATURE("__feature_feature") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Feature:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Feature:", colourToken(FeatureColour.BLUE, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("");
		}
	}, //
	TAG("__feature_tag") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Tags:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return singleLineRule("Tags:", colourToken(FeatureColour.GREEN, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new OptionalUnitPrefixFormattingStrategy(formattingContextSupplier);
		}
	}, //
	COMMENT("__feature_comment") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("#", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return singleLineRule("#", colourToken(FeatureColour.GREEN, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new OptionalUnitPrefixFormattingStrategy(formattingContextSupplier);
		}
	}, //
	BACKGROUND("__feature_background") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Background:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Background:", colourToken(FeatureColour.BLUE, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new StartOfUnitFormattingStrategy(formattingContextSupplier, new FixedIndentFormattingStrategy("  "));
		}
	}, //
	SCENARIO("__feature_scenario") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Scenario:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Scenario:", colourToken(FeatureColour.BLUE, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new StartOfUnitFormattingStrategy(formattingContextSupplier, new FixedIndentFormattingStrategy("  "));
		}
	}, //
	SCENARIO_OUTLINE("__feature_scenario_outline") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Scenario Outline:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Scenario Outline:", colourToken(FeatureColour.BLUE, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new StartOfUnitFormattingStrategy(formattingContextSupplier, new FixedIndentFormattingStrategy("  "));
		}
	}, //
	EXAMPLE("__feature_example") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Examples:", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Examples:", colourToken(FeatureColour.BLUE, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new StartOfUnitFormattingStrategy(formattingContextSupplier, new FixedIndentFormattingStrategy("  "));
		}
	}, //
	EXAMPLE_ROW("__feature_example_row") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("|", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("|", colourToken(FeatureColour.BLACK, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("\t");
		}
	}, //
	GIVEN("__feature_given") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Given", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Given", colourToken(FeatureColour.PINK, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("\t");
		}
	}, //
	WHEN("__feature_when") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("When", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("When", colourToken(FeatureColour.PINK, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("\t ");
		}
	}, //
	THEN("__feature_then") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("Then", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("Then", colourToken(FeatureColour.PINK, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("\t ");
		}
	}, //
	AND("__feature_and") {
		@Override
		public IPredicateRule partitionRule() {
			return singleLineRule("And", id());
		}

		@Override
		public IRule damageRepairerRule(final ColourManager colourManager) {
			return fixedWordRule("And", colourToken(FeatureColour.PINK, colourManager));
		}

		@Override
		public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
			return new FixedIndentFormattingStrategy("\t  ");
		}
	};

	private FeatureContentTypeDefinition(final String id) {
		this.id = id;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public abstract IPredicateRule partitionRule();

	private final String id;

	private static IPredicateRule singleLineRule(final String startString, final String tokenId) {
		final IToken token = new Token(tokenId);
		return new EndOfLineRule(startString, token);
	}

	private static IWordDetector wordStartingWith(final char startChar) {
		return new IWordDetector() {
			@Override
			public boolean isWordStart(final char c) {
				return c == startChar;
			}

			@Override
			public boolean isWordPart(final char c) {
				return c != ',' && c != ' ' && c != '\n';
			}
		};
	}

	private static IWordDetector word(final String word) {
		return new IWordDetector() {
			int i = 0;

			@Override
			public boolean isWordStart(final char c) {
				if (word.charAt(0) == c) {
					i = 0;
					return true;
				}
				return false;
			}

			@Override
			public boolean isWordPart(final char c) {
				i++;
				return word.length() > i && word.charAt(i) == c;
			}
		};
	}

	private static IToken colourToken(final FeatureColour colour, final ColourManager colourManager) {
		return new Token(new TextAttribute(colourManager.getColor(colour.colour())));
	}

	private static IPredicateRule singleLineRule(final String startSequence, final IToken token) {
		return new EndOfLineRule(startSequence, token);
	}

	private static IRule wordRule(final String startSequence, final IToken token) {
		return new WordRule(wordStartingWith(startSequence.charAt(0)), token);
	}

	private static IRule fixedWordRule(final String word, final IToken token) {
		return new WordRule(word(word), token);
	}
}
