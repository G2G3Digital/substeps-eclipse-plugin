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
package com.technophobia.substeps.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.part.FileEditorInput;

import com.technophobia.eclipse.project.ProjectObserver;
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

    private final ProjectObserver projectObserver;


    public SubstepsEditor() {
        this.projectObserver = FeatureEditorPlugin.instance().getProjectObserver();
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
        projectObserver.projectFileChange(project, file);
    }
}
