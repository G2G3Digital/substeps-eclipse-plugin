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
package com.technophobia.substeps.editor.outline.feature;

import java.io.File;

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
    public FeatureElement from(final ProjectFile projectFile) {
        final File file = projectFile.getFile();
        if (file != null) {
            try {
                final FeatureFile featureFile = parser.loadFeatureFile(file);
                return featureElementTransformer.from(featureFile);
            } catch (final Exception ex) {
                FeatureEditorPlugin.instance().error("Couldn't parse feature file " + file.getAbsolutePath(), ex);
            }
        }
        return null;
    }
}
