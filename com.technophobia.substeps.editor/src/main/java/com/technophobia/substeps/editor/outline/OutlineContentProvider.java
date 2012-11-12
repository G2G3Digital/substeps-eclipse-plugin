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

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.model.FeatureFile;
import com.technophobia.substeps.runner.FeatureFileParser;
import com.technophobia.substeps.supplier.Transformer;

public class OutlineContentProvider implements ITreeContentProvider {

    private FeatureElement root = null;
    private IEditorInput input;
    private final IDocumentProvider documentProvider;

    private final Transformer<FeatureFile, FeatureElement> featureElementTransformer;

    protected final static String TAG_POSITIONS = "__tag_positions";
    protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(TAG_POSITIONS);


    public OutlineContentProvider(final Transformer<FeatureFile, FeatureElement> featureElementTransformer,
            final IDocumentProvider provider) {
        this.featureElementTransformer = featureElementTransformer;
        this.documentProvider = provider;
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
    }


    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

        if (oldInput != null) {
            final IDocument document = documentProvider.getDocument(oldInput);
            if (document != null) {
                try {
                    document.removePositionCategory(TAG_POSITIONS);
                } catch (final BadPositionCategoryException x) {
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

                final FeatureElement rootElement = parseEditor(input);
                if (rootElement != null) {
                    root = rootElement;
                }
            }
        }
    }


    private FeatureElement parseEditor(final IEditorInput editorInput) {
        final File file = asFile(editorInput);

        final FeatureElement featureElement = parseFeatureFile(file);
        return featureElement;
    }


    private File asFile(final IEditorInput editorInput) {
        final IFile file = ((FileEditorInput) editorInput).getFile();
        return new File(file.getLocation().makeAbsolute().toOSString());
    }


    private FeatureElement parseFeatureFile(final File file) {
        try {
            final FeatureFileParser parser = new FeatureFileParser();
            final FeatureFile featureFile = parser.loadFeatureFile(file);
            // final OutlineContentHandler contentHandler = new
            // OutlineContentHandler();
            // contentHandler.setPositionCategory(TAG_POSITIONS);
            // contentHandler.setDocumentLocator(new LocatorImpl());
            // xmlParser.setContentHandler(contentHandler);
            // xmlParser.doParse(text);
            return featureElementTransformer.from(featureFile);
        } catch (final Exception ex) {
            FeatureEditorPlugin.error("Couldn't parse feature file " + file.getAbsolutePath(), ex);
            return null;
        }
    }
}
