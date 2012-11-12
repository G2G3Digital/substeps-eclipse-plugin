package com.technophobia.substeps.editor.outline;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.supplier.Transformer;

public class FeatureContentOutlinePage extends ContentOutlinePage {

    private final ITextEditor textEditor;

    private ITreeContentProvider outlineContentProvider = null;
    private ILabelProvider outlineLabelProvider = null;
    private IEditorInput input = null;

    private final Transformer<Integer, Position> lineNumberToPositionTransformer;


    public FeatureContentOutlinePage(final ITextEditor textEditor,
            final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        this.textEditor = textEditor;
        this.lineNumberToPositionTransformer = lineNumberToPositionTransformer;
    }


    @Override
    public void createControl(final Composite parent) {

        super.createControl(parent);
        final TreeViewer viewer = getTreeViewer();
        outlineContentProvider = new OutlineContentProvider(new FeatureFileToElementTransformer(
                lineNumberToPositionTransformer), textEditor.getDocumentProvider());
        viewer.setContentProvider(outlineContentProvider);
        outlineLabelProvider = new OutlineLabelProvider();
        viewer.setLabelProvider(outlineLabelProvider);
        viewer.addSelectionChangedListener(this);

        // control is created after input is set
        if (input != null)
            viewer.setInput(input);
    }


    public void setInput(final Object input) {
        this.input = (IEditorInput) input;
        update();
    }


    /*
     * Change in selection
     */
    @Override
    public void selectionChanged(final SelectionChangedEvent event) {
        super.selectionChanged(event);
        // find out which item in tree viewer we have selected, and set
        // highlight range accordingly

        final ISelection selection = event.getSelection();
        if (selection.isEmpty())
            textEditor.resetHighlightRange();
        else {
            final AbstractModelElement element = (AbstractModelElement) ((IStructuredSelection) selection)
                    .getFirstElement();

            final int start = element.getPosition().getOffset();
            final int length = element.getPosition().getLength();
            try {
                textEditor.setHighlightRange(start, length, true);
            } catch (final IllegalArgumentException x) {
                textEditor.resetHighlightRange();
            }
        }
    }


    public void update() {
        // set the input so that the outlines parse can be called
        // update the tree viewer state
        final TreeViewer viewer = getTreeViewer();

        if (viewer != null) {
            final Control control = viewer.getControl();
            if (control != null && !control.isDisposed()) {
                control.setRedraw(false);
                viewer.setInput(input);
                viewer.expandAll();
                control.setRedraw(true);
            }
        }
    }
}