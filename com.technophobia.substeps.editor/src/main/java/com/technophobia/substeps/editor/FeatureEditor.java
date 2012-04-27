package com.technophobia.substeps.editor;

import java.util.ResourceBundle;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.partition.ContentTypeRuleBasedPartitionScannerFactory;
import com.technophobia.substeps.document.content.view.ContentTypeViewConfiguration;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;
import com.technophobia.substeps.document.formatting.partition.PartitionedFormattingContextFactory;
import com.technophobia.substeps.document.partition.PartitionScannedDocumentProvider;

public class FeatureEditor extends TextEditor {

	private final ColourManager colourManager;

	public FeatureEditor() {

		final ContentTypeDefinitionFactory contentTypeDefinitionFactory = new FeatureContentTypeDefinitionFactory();
		final FormattingContextFactory formattingContextFactory = new PartitionedFormattingContextFactory(contentTypeDefinitionFactory);
		colourManager = new ColourManager();

		setSourceViewerConfiguration(new ContentTypeViewConfiguration(colourManager, contentTypeDefinitionFactory, formattingContextFactory));
		setDocumentProvider(new PartitionScannedDocumentProvider(new ContentTypeRuleBasedPartitionScannerFactory(contentTypeDefinitionFactory)));
	}

	@Override
	public void dispose() {
		colourManager.dispose();
		super.dispose();
	}

	@Override
	protected void createActions() {
		super.createActions();

		final ResourceBundle resourceBundle = FeatureEditorPlugin.instance().getResourceBundle();
		final TextOperationAction action = new TextOperationAction(resourceBundle, "ContentFormatProposal.", this, ISourceViewer.FORMAT);
		setAction("ContentFormatProposal", action);
		getEditorSite().getActionBars().setGlobalActionHandler("ContentFormatProposal", action);
	}
}
