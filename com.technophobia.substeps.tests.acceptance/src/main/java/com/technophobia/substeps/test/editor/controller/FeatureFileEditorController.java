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
package com.technophobia.substeps.test.editor.controller;

import com.technophobia.substeps.test.component.ButtonManagerComponent;
import com.technophobia.substeps.test.component.EditorContainerSWTComponent;
import com.technophobia.substeps.test.component.EditorSWTComponent;
import com.technophobia.substeps.test.component.FormEditorSWTComponent;
import com.technophobia.substeps.test.component.ViewManagerSWTComponent;
import com.technophobia.substeps.test.component.ViewSWTComponent;
import com.technophobia.substeps.test.component.general.GeneralDialogSWTComponent;
import com.technophobia.substeps.test.component.general.TreeItemSWTComponent;
import com.technophobia.substeps.test.component.general.TreeSWTComponent;
import com.technophobia.substeps.test.component.menu.MenuManagerSWTComponent;
import com.technophobia.substeps.test.controller.AbstractSWTController;

public class FeatureFileEditorController extends AbstractSWTController {

    //
    // Commands
    //

    public void createFeatureFileInProject(final String featureFileName, final String projectName) {
        final TreeSWTComponent projectNode = selectProjectNode(projectName);
        projectNode.clickContextMenuItem("New", "File");

        final GeneralDialogSWTComponent dialog = new GeneralDialogSWTComponent("New File");
        dialog.setFocus();
        new FormEditorSWTComponent().textWithLabel("File name:").setText(
                normalizedFeatureFileName(featureFileName) + ".feature");
        new ButtonManagerComponent().buttonFor("Finish").click();
        dialog.loseFocus();

    }


    public void deleteFileInProject(final String featureFileName, final String projectName) {

        final TreeItemSWTComponent featureFileTreeItem = selectProjectNode(projectName).expandNode(projectName).select(
                featureFileName);

        featureFileTreeItem.clickDelete();

        final GeneralDialogSWTComponent dialog = new GeneralDialogSWTComponent("Confirm Delete");
        dialog.setFocus();
        new ButtonManagerComponent().buttonFor("OK").click();
        dialog.loseFocus();
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
