package com.technophobia.substeps.editor.outline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepsContentOutlinePage extends ContentOutlinePage {

    private final ITextEditor textEditor;

    private final ILabelProvider outlineLabelProvider;
    private OutlineContentProvider outlineContentProvider = null;
    private IEditorInput input = null;

    private final Transformer<File, AbstractModelElement> fileToModelTransformer;

    private final Transformer<Position, Integer> positionToLineNumberTransformer;


    public SubstepsContentOutlinePage(final ITextEditor textEditor, final ILabelProvider outlineLabelProvider,
            final Transformer<File, AbstractModelElement> fileToModelTransformer,
            final Transformer<Position, Integer> positionToLineNumberTransformer) {
        this.textEditor = textEditor;
        this.outlineLabelProvider = outlineLabelProvider;
        this.fileToModelTransformer = fileToModelTransformer;
        this.positionToLineNumberTransformer = positionToLineNumberTransformer;
    }


    @Override
    public void createControl(final Composite parent) {

        super.createControl(parent);
        final TreeViewer viewer = getTreeViewer();
        outlineContentProvider = new OutlineContentProvider(fileToModelTransformer, textEditor.getDocumentProvider());
        viewer.setContentProvider(outlineContentProvider);
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


    // @Override
    // public void setSelection(final ISelection selection) {
    // if (selection instanceof TextSelection) {
    // final int line = ((TextSelection) selection).getStartLine();
    // final AbstractModelElement element =
    // outlineContentProvider.getRoot().findItemAtLine(line,
    // positionToLineNumberTransformer);
    // if (element != null) {
    //
    // final TreeViewer viewer = getTreeViewer();
    // final TreeSelection sel = null;
    // final Control control = viewer.getControl();
    // if (control != null && !control.isDisposed()) {
    // control.setRedraw(false);
    // viewer.collapseAll();
    // viewer.setSelection(new TreeSelection(elementAsTreePath(element)),
    // true);
    // viewer.refresh(true);
    // // final viewer.set
    // viewer.expandAll();
    // control.setRedraw(true);
    // }
    // }
    // }
    // super.setSelection(selection);
    // }

    private TreePath[] elementAsTreePath(final AbstractModelElement element) {
        final List<TreePath> elementHierarchy = new ArrayList<TreePath>();
        AbstractModelElement currentElement = element;
        // Check the parent, rather than currentElement, as we don't want the
        // root element - eclipse doesn't render this
        while (currentElement.getParent() != null) {
            elementHierarchy.add(0, new TreePath(new Object[] { currentElement }));
            currentElement = currentElement.getParent();
        }
        return elementHierarchy.toArray(new TreePath[elementHierarchy.size()]);
    }
}
