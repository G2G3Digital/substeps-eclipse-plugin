package com.technophobia.substeps.document.content;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.formatting.ContextAwareContentFormatter;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;
import com.technophobia.substeps.document.formatting.strategy.DefaultFormattingStrategy;

public class ContentTypeViewConfiguration extends SourceViewerConfiguration {

	private final ColourManager colourManager;
	private final Map<String, ContentTypeDefinition> contentTypeDefinitionMap;
	private final FormattingContextFactory formattingContextFactory;

	public ContentTypeViewConfiguration(final ColourManager colourManager,
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory,
			final FormattingContextFactory formattingContextFactory) {
		this.colourManager = colourManager;
		this.formattingContextFactory = formattingContextFactory;

		this.contentTypeDefinitionMap = buildViewConfigurationStatusFrom(contentTypeDefinitionFactory);
	}

	@Override
	public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
		final Set<String> configuredContentTypes = contentTypeDefinitionMap
				.keySet();
		return configuredContentTypes.toArray(new String[configuredContentTypes
				.size()]);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			final ISourceViewer sourceViewer) {
		final PresentationReconciler reconciler = new PresentationReconciler();

		for (final Map.Entry<String, ContentTypeDefinition> entry : contentTypeDefinitionMap
				.entrySet()) {
			if (entry.getValue() != null) {
				setDamagerRepairer(entry.getKey(), entry.getValue()
						.damageRepairerRule(colourManager), reconciler);
			}
		}

		return reconciler;
	}

	@Override
	public IContentFormatter getContentFormatter(
			final ISourceViewer sourceViewer) {
		final ContextAwareContentFormatter formatter = new ContextAwareContentFormatter(
				formattingContextFactory);
		for (final Map.Entry<String, ContentTypeDefinition> entry : contentTypeDefinitionMap
				.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				formatter.setFormattingStrategy(entry.getValue()
						.formattingStrategy(formatter), entry.getKey());
			}
		}
		formatter.setFormattingStrategy(new DefaultFormattingStrategy(),
				IDocument.DEFAULT_CONTENT_TYPE);
		return formatter;
	}

	private void setDamagerRepairer(final String contentType, final IRule rule,
			final PresentationReconciler reconciler) {
		final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				getElementScanner(rule));
		reconciler.setDamager(dr, contentType);
		reconciler.setRepairer(dr, contentType);
	}

	private Map<String, ContentTypeDefinition> buildViewConfigurationStatusFrom(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {

		final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory
				.contentTypeDefinitions();

		final Map<String, ContentTypeDefinition> results = new HashMap<String, ContentTypeDefinition>();
		results.put(IDocument.DEFAULT_CONTENT_TYPE, null);

		for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
			results.put(contentTypeDefinition.id(), contentTypeDefinition);
		}

		return Collections.unmodifiableMap(results);
	}

	private ITokenScanner getElementScanner(final IRule rule) {
		return new SingleRuleBasedScannerElementScanner(rule);
	}
}
