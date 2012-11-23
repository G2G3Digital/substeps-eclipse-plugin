package com.technophobia.substeps.document.content.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextEditor;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.assist.ContentAssistantFactory;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.ContextAwareContentFormatter;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;
import com.technophobia.substeps.document.formatting.strategy.NullFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.document.text.rule.word.AnySingleWordDetector;
import com.technophobia.substeps.supplier.Supplier;

/**
 * SourceViewerConfiguration for rendering {@link TextEditor}s using a
 * {@link ContentTypeDefinitionFactory}
 * 
 * @author sforbes
 * 
 */
public class ContentTypeViewConfiguration extends SourceViewerConfiguration {

    private final ColourManager colourManager;
    private final FormattingContextFactory formattingContextFactory;

    private Map<String, ContentTypeDefinition> contentTypeDefinitionMap = null;
    private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;
    private final ContentAssistantFactory contentAssistantFactory;
    private final Supplier<PartitionContext> partitionContextSupplier;


    public ContentTypeViewConfiguration(final ColourManager colourManager,
            final ContentTypeDefinitionFactory contentTypeDefinitionFactory,
            final FormattingContextFactory formattingContextFactory,
            final ContentAssistantFactory contentAssistantFactory,
            final Supplier<PartitionContext> partitionContextSupplier) {
        this.colourManager = colourManager;
        this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
        this.formattingContextFactory = formattingContextFactory;
        this.contentAssistantFactory = contentAssistantFactory;
        this.partitionContextSupplier = partitionContextSupplier;
    }


    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
        final Set<String> configuredContentTypes = definitionMap().keySet();
        return configuredContentTypes.toArray(new String[configuredContentTypes.size()]);
    }


    @Override
    public IPresentationReconciler getPresentationReconciler(final ISourceViewer sourceViewer) {
        final PresentationReconciler reconciler = new PresentationReconciler();

        for (final Map.Entry<String, ContentTypeDefinition> entry : definitionMap().entrySet()) {
            if (entry.getValue() != null) {
                setDamagerRepairer(entry.getKey(),
                        entry.getValue().damageRepairerRule(colourManager, partitionContextSupplier), reconciler);
            }
        }

        setDamagerRepairer(IDocument.DEFAULT_CONTENT_TYPE, defaultDamageRepairer(), reconciler);
        return reconciler;
    }


    private IRule defaultDamageRepairer() {
        final Token token = new Token(new TextAttribute(colourManager.getColor(FeatureColour.BLACK.colour())));
        return new WordRule(new AnySingleWordDetector(), token);
    }


    @Override
    public IContentFormatter getContentFormatter(final ISourceViewer sourceViewer) {
        final ContextAwareContentFormatter formatter = new ContextAwareContentFormatter(formattingContextFactory);
        for (final Map.Entry<String, ContentTypeDefinition> entry : definitionMap().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                formatter.setFormattingStrategy(entry.getValue().formattingStrategy(formatter), entry.getKey());
            }
        }
        formatter.setFormattingStrategy(new NullFormattingStrategy(), IDocument.DEFAULT_CONTENT_TYPE);
        return formatter;
    }


    @Override
    public IContentAssistant getContentAssistant(final ISourceViewer sourceViewer) {
        return contentAssistantFactory.createContentAssist();
    }


    private Map<String, ContentTypeDefinition> definitionMap() {
        if (contentTypeDefinitionMap == null) {
            contentTypeDefinitionMap = buildViewConfigurationStatusForContentTypes();
        }
        return contentTypeDefinitionMap;
    }


    private void setDamagerRepairer(final String contentType, final IRule rule, final PresentationReconciler reconciler) {
        final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getElementScanner(rule));
        reconciler.setDamager(dr, contentType);
        reconciler.setRepairer(dr, contentType);
    }


    private Map<String, ContentTypeDefinition> buildViewConfigurationStatusForContentTypes() {

        final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory.contentTypeDefinitions();

        final Map<String, ContentTypeDefinition> results = new HashMap<String, ContentTypeDefinition>();
        results.put(IDocument.DEFAULT_CONTENT_TYPE, null);

        for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
            results.put(contentTypeDefinition.id(), contentTypeDefinition);
        }

        return Collections.unmodifiableMap(results);
    }


    private ITokenScanner getElementScanner(final IRule rule) {
        return new SingleRuleBasedElementScanner(rule);
    }
}
