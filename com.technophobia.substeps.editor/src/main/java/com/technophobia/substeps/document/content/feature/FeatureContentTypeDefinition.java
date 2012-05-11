/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.content.feature;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
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

	FEATURE("__feature_feature", false) {
		@Override
		public IPredicateRule partitionRule() {
			return paragraphRule("Feature:", id());
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
	TAG("__feature_tag", true) {
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
	COMMENT("__feature_comment", true) {
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
	BACKGROUND("__feature_background", false) {
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
	SCENARIO("__feature_scenario", false) {
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
	SCENARIO_OUTLINE("__feature_scenario_outline", false) {
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
	EXAMPLE("__feature_example", false) {
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
	EXAMPLE_ROW("__feature_example_row", false) {
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
	GIVEN("__feature_given", false) {
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
	WHEN("__feature_when", false) {
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
	THEN("__feature_then", false) {
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
	AND("__feature_and", false) {
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

	private FeatureContentTypeDefinition(final String id, final boolean optional) {
		this.id = id;
		this.optional = optional;
	}

	@Override
	public String id() {
		return id;
	}
	
	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public abstract IPredicateRule partitionRule();

	private final String id;
	private final boolean optional;

	private static IPredicateRule singleLineRule(final String startString, final String tokenId) {
		final IToken token = new Token(tokenId);
		return new EndOfLineRule(startString, token);
	}
	
	private static IPredicateRule paragraphRule(final String startString, final String tokenId) {
		final IToken token = new Token(tokenId);
		return new MultiLineRule(startString, "\n\n", token);
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
