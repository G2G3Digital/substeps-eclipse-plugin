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
package com.technophobia.substeps.test.editor.steps;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.test.controller.ProjectSWTController;
import com.technophobia.substeps.test.editor.controller.FeatureFileEditorController;
import com.technophobia.substeps.test.steps.AbstractSteps;
import com.technophobia.substeps.test.steps.SWTBotInitialiser;

@StepImplementations(requiredInitialisationClasses = { EditorBeforeAndAfterExecutor.class, SWTBotInitialiser.class })
public class ContentFormattingSteps extends AbstractSteps {

    private static final Logger LOG = LoggerFactory.getLogger(ContentFormattingSteps.class);

    private static final String NEWLINE = System.getProperty("line.separator");


    @Step("Given I am working in a new editor named \"([^\"]*)\" in project \"([^\"]*)\"")
    public void ensureNewEditor(final String featureFileName, final String projectName) {
        LOG.debug("Given I am working in a new editor named " + featureFileName + " in project " + projectName);

        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        if (controller.doesFeatureFileExistForProject(featureFileName, projectName)) {
            controller.deleteFileInProject(featureFileName, projectName);
        }
        controller.createFeatureFileInProject(featureFileName, projectName);

        new ProjectSWTController().selectFileToEdit(projectName, featureFileName);
    }


    @Step("Given the text from file \"([^\"]*)\" exists in the editor")
    public void insertTextFromFileToEditor(final String fileName) {
        LOG.debug("Given the text from file " + fileName + " exists in the editor");

        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        controller.setEditorContentsTo(textFromFile(fileName));
    }


    @Step("When I format the contents of the editor")
    public void formatEditorContents() {
        LOG.debug("When I format the contents of the editor");

        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        controller.formatContent();
    }


    @Step("Then the editor contents match file \"([^\"]*)\"")
    public void assertEditorContentsMatch(final String fileName) {
        LOG.debug("Then the editor contents match file " + fileName);
        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        final String textFromFile = removeTrailingNewlines(textFromFile(fileName));
        final String textFromEditor = removeTrailingNewlines(controller.currentEditorContents());

        LOG.debug("About to check that text from editor [" + textFromEditor + "] matches expected text from file ["
                + textFromFile + "] in file [" + fileName + "]");

        assertEquals("editor contents are not the same as the contents in " + fileName, textFromFile, textFromEditor);
    }


    private String removeTrailingNewlines(final String text) {
        int index = text.length() - 1;
        while (isNewline(text, index))
            index -= NEWLINE.length();

        return text.substring(0, index + 1);
    }


    private boolean isNewline(final String content, final int endPos) {
        return content.substring(endPos - NEWLINE.length(), endPos).equals(NEWLINE);
    }


    private String textFromFile(final String fileName) {
        try {
            final InputStream stream = getClass().getResourceAsStream("/featuresnippets/" + fileName);
            if (stream == null) {
                throw new IOException("Could not get contents from file " + fileName);
            }
            return textFromStream(stream);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not read file " + fileName, ex);
        }
    }


    private String textFromStream(final InputStream stream) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int val = -1;
        while ((val = stream.read()) != -1) {
            sb.append((char) val);
        }

        return sb.toString();
    }
}
