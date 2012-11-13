package com.technophobia.substeps.editor;

import java.io.File;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.SubstepsContentDefinitionFactory;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.substeps.FileToSubstepDefinitionElementTransformer;
import com.technophobia.substeps.supplier.Transformer;

/**
 * Subclass of {@link FeatureEditor} used to edit files with the .substeps
 * extension
 * 
 * @author sforbes
 * 
 */
public class SubstepsEditor extends FeatureEditor {

    @Override
    protected ContentTypeDefinitionFactory contentTypeDefinitionFactory() {
        return new SubstepsContentDefinitionFactory();
    }


    @Override
    protected Transformer<File, AbstractModelElement> fileToModelTransformer() {
        return new FileToSubstepDefinitionElementTransformer(lineNumberToDocumentOffset());
    }
}
