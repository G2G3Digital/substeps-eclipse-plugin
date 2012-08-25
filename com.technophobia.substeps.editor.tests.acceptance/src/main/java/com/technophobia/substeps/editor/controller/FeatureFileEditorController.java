package com.technophobia.substeps.editor.controller;

import com.technophobia.substeps.editor.component.ButtonManagerComponent;
import com.technophobia.substeps.editor.component.EditorContainerSWTComponent;
import com.technophobia.substeps.editor.component.EditorSWTComponent;
import com.technophobia.substeps.editor.component.FormEditorSWTComponent;
import com.technophobia.substeps.editor.component.ViewManagerSWTComponent;
import com.technophobia.substeps.editor.component.ViewSWTComponent;
import com.technophobia.substeps.editor.component.general.GeneralDialogSWTComponent;
import com.technophobia.substeps.editor.component.general.TreeItemSWTComponent;
import com.technophobia.substeps.editor.component.general.TreeSWTComponent;
import com.technophobia.substeps.editor.component.menu.MenuManagerSWTComponent;

public class FeatureFileEditorController extends AbstractSWTController {

    //
    // Commands
    //

    public void createFeatureFileInProject(final String featureFileName, final String projectName) {
        final TreeSWTComponent projectNode = selectProjectNode(projectName);
        projectNode.clickContextMenuItem("New", "File");

        new GeneralDialogSWTComponent().setFocus("New File");
        new FormEditorSWTComponent().textWithLabel("File name:").setText(
                normalizedFeatureFileName(featureFileName) + ".feature");
        new ButtonManagerComponent().buttonFor("Finish").click();

        try {
            Thread.sleep(4000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void deleteFileInProject(final String featureFileName, final String projectName) {

        final TreeItemSWTComponent featureFileTreeItem = selectProjectNode(projectName).expandNode(projectName).select(
                featureFileName);

        featureFileTreeItem.clickDelete();

        new GeneralDialogSWTComponent().setFocus("Confirm Delete");
        new ButtonManagerComponent().buttonFor("OK").click();

        try {
            Thread.sleep(4000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void setEditorContentsTo(final String text) {

        final EditorSWTComponent editorComponent = new EditorSWTComponent();
        editorComponent.setContentsTo(text);
        editorComponent.save();
    }


    public void formatContent() {
        // SWTTestUtil.setMainFrameToActiveShellHack();
        new MenuManagerSWTComponent().menuFor("Edit").menuFor("Content Format").click();

        new EditorSWTComponent().save();
    }


    public void closeAllOpenEditors() {
        new EditorContainerSWTComponent().closeAll();
    }


    //
    // Queries
    //
    public boolean doesFeatureFileExistForProject(final String featureFileName, final String projectName) {

        final TreeItemSWTComponent projectNode = selectProjectNode(projectName).expandNode(projectName);

        return projectNode.isItemExist(featureFileName);
    }


    public String currentEditorContents() {
        return new EditorSWTComponent().content();
    }


    private TreeSWTComponent selectProjectNode(final String projectName) {
        final ViewSWTComponent view = new ViewManagerSWTComponent().viewByTitle("Package Explorer");
        final TreeSWTComponent tree = view.treeInView();

        return tree.select(projectName);
    }


    private String normalizedFeatureFileName(final String featureFileName) {
        if (featureFileName.endsWith(".feature")) {
            return featureFileName.substring(0, featureFileName.length() - ".feature".length());
        }
        return featureFileName;
    }
}
