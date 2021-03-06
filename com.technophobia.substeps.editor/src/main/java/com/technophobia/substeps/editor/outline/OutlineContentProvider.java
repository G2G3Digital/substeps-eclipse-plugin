/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.editor.outline;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.substeps.ProjectFile;
import com.technophobia.substeps.supplier.Transformer;

public class OutlineContentProvider implements ITreeContentProvider {

    private AbstractModelElement root = null;
    private IEditorInput input;
    private final IDocumentProvider documentProvider;

    private final Transformer<ProjectFile, AbstractModelElement> fileToElementTransformer;

    protected final static String TAG_POSITIONS = "__tag_positions";
    protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(TAG_POSITIONS);


    public OutlineContentProvider(final Transformer<ProjectFile, AbstractModelElement> fileToElementTransformer,
            final IDocumentProvider provider) {
        this.fileToElementTransformer = fileToElementTransformer;
        this.documentProvider = provider;
    }


    public AbstractModelElement getRoot() {
        return root;
    }


    @Override
    public Object[] getChildren(final Object parentElement) {
        if (parentElement == input) {
            if (root == null)
                return new Object[0];
            final Collection<AbstractModelElement> children = root.getChildren();
            if (children != null)
                return children.toArray();
        } else {
            final AbstractModelElement parent = (AbstractModelElement) parentElement;
            final Collection<AbstractModelElement> children = parent.getChildren();
            if (children != null)
                return children.toArray();
        }
        return new Object[0];
    }


    @Override
    public Object getParent(final Object element) {
        if (element instanceof AbstractModelElement) {
            return ((AbstractModelElement) element).getParent();
        }
        return null;
    }


    @Override
    public boolean hasChildren(final Object element) {
        if (element == input) {
            return true;
        }
        return ((AbstractModelElement) element).getChildren().size() > 0;
    }


    @Override
    public Object[] getElements(final Object inputElement) {
        if (root == null)
            return new Object[0];
        final Collection<AbstractModelElement> children = root.getChildren();
        if (children != null) {
            return children.toArray();
        }
        return new Object[0];
    }


    @Override
    public void dispose() {
        // No-op
    }


    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

        if (oldInput != null) {
            final IDocument document = documentProvider.getDocument(oldInput);
            if (document != null) {
                try {
                    document.removePositionCategory(TAG_POSITIONS);
                } catch (final BadPositionCategoryException x) {
                    // No-op
                }
                document.removePositionUpdater(positionUpdater);
            }
        }

        input = (IEditorInput) newInput;

        if (newInput != null) {
            final IDocument document = documentProvider.getDocument(newInput);
            if (document != null) {
                document.addPositionCategory(TAG_POSITIONS);
                document.addPositionUpdater(positionUpdater);

                final AbstractModelElement rootElement = parseEditor(input);
                if (rootElement != null) {
                    root = rootElement;
                }
            }
        }
    }


    private AbstractModelElement parseEditor(final IEditorInput editorInput) {
        final IFile iFile = ((FileEditorInput) editorInput).getFile();
        if (iFile != null) {
            final File file = asFile(iFile);
            final AbstractModelElement element = fileToElementTransformer
                    .from(new ProjectFile(iFile.getProject(), file));
            return element;
        }
        return null;
    }


    private File asFile(final IFile iFile) {
        if (iFile != null && iFile.getLocation() != null) {
            return new File(iFile.getLocation().makeAbsolute().toOSString());
        }
        return null;
    }
}
