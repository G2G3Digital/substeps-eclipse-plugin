package com.technophobia.substeps.editor.outline.feature;

import org.eclipse.jface.text.Position;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.editor.outline.substeps.ProjectFile;
import com.technophobia.substeps.model.FeatureFile;
import com.technophobia.substeps.runner.FeatureFileParser;
import com.technophobia.substeps.supplier.Transformer;

public class FileToFeatureElementTransformer implements Transformer<ProjectFile, AbstractModelElement> {

    private final Transformer<FeatureFile, FeatureElement> featureElementTransformer;
    private final FeatureFileParser parser;


    public FileToFeatureElementTransformer(final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        this.featureElementTransformer = new FeatureFileToElementTransformer(lineNumberToPositionTransformer);
        this.parser = new FeatureFileParser();
    }


    @Override
    public FeatureElement from(final ProjectFile file) {
        try {
            final FeatureFile featureFile = parser.loadFeatureFile(file.getFile());
            return featureElementTransformer.from(featureFile);
        } catch (final Exception ex) {
            FeatureEditorPlugin.instance().error("Couldn't parse feature file " + file.getFile().getAbsolutePath(), ex);
            return null;
        }
    }
}
