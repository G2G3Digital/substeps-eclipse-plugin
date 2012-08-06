package com.technophobia.substeps.editor.steps;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.editor.SWTTestUtil;
import com.technophobia.substeps.editor.controller.FeatureFileEditorController;
import com.technophobia.substeps.editor.controller.ProjectSWTController;
import com.technophobia.substeps.model.SubSteps.Step;

public class ContentFormattingSteps extends AbstractEditorSteps {

    private static final Logger LOG = LoggerFactory.getLogger(ContentFormattingSteps.class);

    private static final String NEWLINE = System.getProperty("line.separator");


    @Step("Given I am working on the \"([^\"]*)\" Perspective")
    public void navigateToPerspective(final String perspectiveName) {

        final ProjectSWTController controller = createController(ProjectSWTController.class);
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        controller.navigateToPerspective(perspectiveName);
    }


    @Step("And dummy")
    public void andSomething() {
        // no-op
    }


    @Step("Given there is a general project named \"([^\"]*)\"")
    public void ensureProjectExistsNamed(final String projectName) {
        // TODO - add logging

        final ProjectSWTController controller = createController(ProjectSWTController.class);

        if (!controller.isProjectExist(projectName)) {
            SWTTestUtil.setMainFrameToActiveShellHack();
            controller.createGeneralProject(projectName);
        }
    }


    @Step("Given I am working in a new editor named \"([^\"]*)\" in project \"([^\"]*)\"")
    public void ensureNewEditor(final String featureFileName, final String projectName) {

        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        if (controller.doesFeatureFileExistForProject(featureFileName, projectName)) {
            controller.deleteFileInProject(featureFileName, projectName);
        }
        controller.createFeatureFileInProject(featureFileName, projectName);

        new ProjectSWTController().selectFileToEdit(projectName, featureFileName);
    }


    @Step("Given the text from file \"([^\"]*)\" exists in the editor")
    public void insertTextFromFileToEditor(final String fileName) {

        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        controller.setEditorContentsTo(textFromFile(fileName));
    }


    @Step("When I format the contents of the editor")
    public void formatEditorContents() {
        final FeatureFileEditorController controller = createController(FeatureFileEditorController.class);

        controller.formatContent();
    }


    @Step("Then the editor contents match file \"([^\"]*)\"")
    public void assertEditorContentsMatch(final String fileName) {
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
