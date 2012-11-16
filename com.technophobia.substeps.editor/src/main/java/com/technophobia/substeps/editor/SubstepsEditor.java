package com.technophobia.substeps.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.part.FileEditorInput;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.SubstepsContentDefinitionFactory;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.substeps.FileToSubstepDefinitionElementTransformer;
import com.technophobia.substeps.editor.outline.substeps.ProjectFile;
import com.technophobia.substeps.supplier.Transformer;

/**
 * Subclass of {@link FeatureEditor} used to edit files with the .substeps
 * extension
 * 
 * @author sforbes
 * 
 */
public class SubstepsEditor extends FeatureEditor {

    private final ProjectManager projectManager;


    public SubstepsEditor() {
        this.projectManager = FeatureEditorPlugin.instance().getProjectManager();
    }


    @Override
    protected ContentTypeDefinitionFactory contentTypeDefinitionFactory() {
        return new SubstepsContentDefinitionFactory();
    }


    @Override
    protected Transformer<ProjectFile, AbstractModelElement> fileToModelTransformer() {
        return new FileToSubstepDefinitionElementTransformer(lineNumberToDocumentOffset());
    }


    @Override
    protected void editorSaved() {
        super.editorSaved();

        final IFile file = ((FileEditorInput) getEditorInput()).getFile();
        final IProject project = file.getProject();
        projectManager.projectFileChanged(project, file);
    }
}
