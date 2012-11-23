package com.technophobia.substeps.editor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

import com.technophobia.substeps.FeatureEditorPlugin;

/**
 * All actions required for the feature editor
 * 
 * @author sforbes
 * 
 */
public class FeatureEditorActionContributor extends TextEditorActionContributor {

    private final RetargetTextEditorAction contentFormatProposal;


    public FeatureEditorActionContributor() {
        this.contentFormatProposal = new RetargetTextEditorAction(FeatureEditorPlugin.instance().getResourceBundle(),
                "ContentFormatProposal.");
    }


    @Override
    public void dispose() {
        super.dispose();
    }


    @Override
    public void contributeToMenu(final IMenuManager menuManager) {
        super.contributeToMenu(menuManager);

        final IMenuManager editMenu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
        if (editMenu != null) {
            editMenu.add(new Separator());
            editMenu.add(contentFormatProposal);
        }
    }


    @Override
    public void setActiveEditor(final IEditorPart part) {

        super.setActiveEditor(part);

        ITextEditor editor = null;
        if (part instanceof FeatureEditor) {
            editor = (ITextEditor) part;
        }

        contentFormatProposal.setAction(getAction(editor, "ContentFormatProposal"));
    }
}
