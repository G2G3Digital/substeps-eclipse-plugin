package com.technophobia.substeps.editor.controller;

import com.technophobia.substeps.editor.SWTTestUtil;
import com.technophobia.substeps.editor.component.ButtonManagerComponent;
import com.technophobia.substeps.editor.component.EditorSWTComponent;
import com.technophobia.substeps.editor.component.FormEditorSWTComponent;
import com.technophobia.substeps.editor.component.ViewManagerSWTComponent;
import com.technophobia.substeps.editor.component.general.GeneralDialogSWTComponent;
import com.technophobia.substeps.editor.component.general.TableSWTComponent;
import com.technophobia.substeps.editor.component.general.TreeItemSWTComponent;
import com.technophobia.substeps.editor.component.general.TreeSWTComponent;
import com.technophobia.substeps.editor.component.menu.MenuManagerSWTComponent;
import com.technophobia.substeps.editor.component.toolbar.ToolbarSWTComponent;

public class ProjectSWTController extends AbstractSWTController {

	//
	// Commands
	//

	public void navigateToPerspective(final String perspectiveName) {
		SWTTestUtil.setMainFrameToActiveShellHack();
		new MenuManagerSWTComponent().menuFor("Window")
				.menuFor("Open Perspective").menuFor("Other...").click();

		final GeneralDialogSWTComponent dialog = new GeneralDialogSWTComponent();
		dialog.setFocus("Open Perspective");

		final TableSWTComponent table = dialog.table();
		if (table.hasItem(perspectiveName)) {
			table.select(perspectiveName);
		} else {
			table.select(perspectiveName + " (default)");
		}

		new ButtonManagerComponent().buttonFor("OK").click();
	}

	public void createGeneralProject(final String projectName) {

		final GeneralDialogSWTComponent dialogComponent = new GeneralDialogSWTComponent();
		final ButtonManagerComponent buttonManagerComponent = new ButtonManagerComponent();

		openNewDialog(dialogComponent);

		selectNewProjectTreeItem(dialogComponent, buttonManagerComponent);

		editProject(projectName, buttonManagerComponent);
	}

	public void selectFileToEdit(final String... pathToFile) {
		final TreeSWTComponent packageExplorer = new ViewManagerSWTComponent()
				.viewByTitle("Package Explorer").treeInView();

		TreeItemSWTComponent item = packageExplorer.expandNode(pathToFile[0]);
		for (int i = 1; i < pathToFile.length; i++) {
			item = item.select(pathToFile[i]);
			item.expand();
		}

		item.doubleClick();

		new EditorSWTComponent().focus();
	}

	//
	// Queries
	//

	public boolean isProjectExist(final String projectName) {
		final TreeSWTComponent packageExplorer = new ViewManagerSWTComponent()
				.viewByTitle("Package Explorer").treeInView();

		return packageExplorer.isItemExist(projectName);
	}

	private void openNewDialog(final GeneralDialogSWTComponent dialogComponent) {
		new ToolbarSWTComponent().buttonForTooltip("New").click();

		dialogComponent.setFocus("New");

	}

	private void selectNewProjectTreeItem(
			final GeneralDialogSWTComponent dialogComponent,
			final ButtonManagerComponent buttonManagerComponent) {
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SWTTestUtil.setActiveShellHack("New");
		dialogComponent.tree().select("General").expandNode("General")
				.select("Project");

		SWTTestUtil.setActiveShellHack("New");
		buttonManagerComponent.buttonFor("Next >").click();
		dialogComponent.setFocus("New Project");

	}

	private void editProject(final String projectName,
			final ButtonManagerComponent buttonManagerComponent) {
		new FormEditorSWTComponent().textWithLabel("Project name:").setText(
				projectName);

		buttonManagerComponent.buttonFor("Finish").click();

		try {
			Thread.sleep(4000);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}