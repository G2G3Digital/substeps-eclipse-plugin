package com.technophobia.substeps.editor.outline.feature;

import java.io.File;

import org.eclipse.jface.text.Position;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.model.FeatureFile;
import com.technophobia.substeps.runner.FeatureFileParser;
import com.technophobia.substeps.supplier.Transformer;

public class FileToFeatureElementTransformer implements Transformer<File, AbstractModelElement> {

    private final Transformer<FeatureFile, FeatureElement> featureElementTransformer;
    private final FeatureFileParser parser;


    public FileToFeatureElementTransformer(final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        this.featureElementTransformer = new FeatureFileToElementTransformer(lineNumberToPositionTransformer);
        this.parser = new FeatureFileParser();
    }


    @Override
    public FeatureElement from(final File file) {
        try {
            final FeatureFile featureFile = parser.loadFeatureFile(file);
            return featureElementTransformer.from(featureFile);
        } catch (final Exception ex) {
            FeatureEditorPlugin.error("Couldn't parse feature file " + file.getAbsolutePath(), ex);
            return null;
        }
    }
}
