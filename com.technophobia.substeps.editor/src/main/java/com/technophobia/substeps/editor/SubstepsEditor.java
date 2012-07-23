package com.technophobia.substeps.editor;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.SubstepsContentDefinitionFactory;

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
}
