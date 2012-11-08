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
package com.technophobia.substeps.editor;

import java.util.ResourceBundle;

import org.eclipse.core.commands.contexts.IContextManagerListener;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

import com.technophobia.eclipse.editor.FormattableEditorPart;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.assist.AutoActivatingContentAssistantDecorator;
import com.technophobia.substeps.document.content.assist.ContentAssistantFactory;
import com.technophobia.substeps.document.content.assist.ProcessedContentAssistantFactory;
import com.technophobia.substeps.document.content.assist.feature.StepImplementationProcessorSupplier;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.partition.ContentTypeRuleBasedPartitionScannerFactory;
import com.technophobia.substeps.document.content.view.ContentTypeViewConfiguration;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;
import com.technophobia.substeps.document.formatting.partition.PartitionedFormattingContextFactory;
import com.technophobia.substeps.document.partition.PartitionScannedDocumentProvider;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

/**
 * TextEditor whose text is configured to view as a feature file
 * 
 * @author sforbes
 * 
 */
public class FeatureEditor extends TextEditor implements FormattableEditorPart, IPartListener2 {

    private final ColourManager colourManager;
    private IContextActivation currentActivateContext;

    @SuppressWarnings("unchecked")
    public FeatureEditor() {

        final ContentTypeDefinitionFactory contentTypeDefinitionFactory = contentTypeDefinitionFactory();
        final FormattingContextFactory formattingContextFactory = new PartitionedFormattingContextFactory(
                contentTypeDefinitionFactory);
        final ContentAssistantFactory contentAssistantFactory = new ProcessedContentAssistantFactory(
                processorSupplier(), (Callback1<IContentAssistant>) new AutoActivatingContentAssistantDecorator());
        colourManager = new ColourManager();

        setSourceViewerConfiguration(new ContentTypeViewConfiguration(colourManager, contentTypeDefinitionFactory,
                formattingContextFactory, contentAssistantFactory));
        setDocumentProvider(new PartitionScannedDocumentProvider(new ContentTypeRuleBasedPartitionScannerFactory(
                contentTypeDefinitionFactory)));
        
        FeatureEditorPlugin.info("ctor");
    }


    @Override
    public void doFormat() {
        ((SourceViewer) getSourceViewer()).doOperation(ISourceViewer.FORMAT);
    }


    /**
     * Return a new {@link ContentTypeDefinitionFactory}
     * 
     * @return
     */
    protected ContentTypeDefinitionFactory contentTypeDefinitionFactory() {
        return new FeatureContentTypeDefinitionFactory();
    }


    /**
     * Return a new {@link Supplier} of type {@link IContentAssistProcessor}
     * 
     * @return
     */
    private Supplier<IContentAssistProcessor> processorSupplier() {
        return new Supplier<IContentAssistProcessor>() {
            @Override
            public IContentAssistProcessor get() {
                return new StepImplementationProcessorSupplier(getSite(), FeatureEditorPlugin.instance()
                        .getSuggestionManager()).get();
            }

        };
    }

    private void activateContext(){
    	if (currentActivateContext == null) {
			final IContextService contextService = (IContextService) this.getSite().getWorkbenchWindow().getService(
					IContextService.class);
			currentActivateContext = contextService.activateContext(SUBSTEPS_CONTEXT);
    	}
    	// else we're already active 
    }
    
    private void deactivateContext(){
        if (currentActivateContext != null) {
	        final IContextService contextService = (IContextService) this.getSite().getWorkbenchWindow().getService(
					IContextService.class);
	        
	    	contextService.deactivateContext(currentActivateContext);
	    	currentActivateContext = null;
        }
    }
    
    @Override
    public void dispose() {
        colourManager.dispose();
    	
        
		IPartService partService = (IPartService) this.getSite().getService(IPartService.class);
		
		partService.removePartListener(this);
        
        super.dispose();
    }


    @Override
    protected void createActions() {
        super.createActions();

        final ResourceBundle resourceBundle = FeatureEditorPlugin.instance().getResourceBundle();
        final TextOperationAction action = new TextOperationAction(resourceBundle, "ContentFormatProposal.", this,
                ISourceViewer.FORMAT);
        setAction("ContentFormatProposal", action);
        getEditorSite().getActionBars().setGlobalActionHandler("ContentFormatProposal", action);
    }
    
    private static final String SUBSTEPS_CONTEXT = "com.technophobia.substeps.editor.SubstepsContext";
    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void setSite(final IWorkbenchPartSite site){

		super.setSite(site);
		
		// add an activation listener
		
		IPartService partService = (IPartService) site.getService(IPartService.class);
		
		partService.addPartListener(this);
		
	}
	

	public IDocument getCurrentDocument() {
		PartitionScannedDocumentProvider docProvider = (PartitionScannedDocumentProvider)getDocumentProvider();
		return docProvider.getDocuemnt();
	}


	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
			activateContext();
		}
	}


	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
			activateContext();
		}
	}


	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
			deactivateContext();
		}
	}


	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
			deactivateContext();
		}
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
			activateContext();
			}
	}


	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		
		if (getEditorSite().getId().equals( partRef.getId())){
				deactivateContext();
			}
	}


	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// beware you get LOADS of these, to the point where it doesn't really stop when running eclipse via eclipse (during plugin dev)
		
	}


	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// not used yet... ?
	}
}
