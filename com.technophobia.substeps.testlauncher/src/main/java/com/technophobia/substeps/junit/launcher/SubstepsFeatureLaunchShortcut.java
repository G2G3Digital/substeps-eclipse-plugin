package com.technophobia.substeps.junit.launcher;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.JUnitMigrationDelegate;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

@SuppressWarnings("restriction")
public class SubstepsFeatureLaunchShortcut extends JUnitLaunchShortcut {

	private static final String LAUNCH_CONFIG_ID = "com.technophobia.substeps.junit.launchconfig";

	@Override
	public void launch(final org.eclipse.ui.IEditorPart editor,
			final String mode) {
		final ILaunchConfigurationWorkingCopy workingCopy = createLaunchConfigWorkingCopyFor(
				editor, mode);
		final ILaunchConfiguration config = configFromWorkingCopy(workingCopy);
		DebugUITools.launch(config, mode);

	}

	private ILaunchConfiguration configFromWorkingCopy(
			final ILaunchConfigurationWorkingCopy workingCopy) {
		// TODO - check if there's an existing configuration for this working
		// copy - if so, launch it, otherwise doSave()
		try {
			return workingCopy.doSave();
		} catch (final CoreException e) {
			ExceptionHandler
					.handle(e,
							getParentShell(),
							SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_dialog_title,
							SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_error_launch);
			return null;
		}
	}

	private ILaunchConfigurationWorkingCopy createLaunchConfigWorkingCopyFor(
			final IEditorPart editor, final String mode) {
		final ILaunchConfigurationType configType = launchManager()
				.getLaunchConfigurationType(LAUNCH_CONFIG_ID);
		try {
			final ILaunchConfigurationWorkingCopy wc = configType.newInstance(
					null,
					launchManager().generateLaunchConfigurationName(
							editor.getTitle()));
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					"com.technophobia.substeps.DefinableFeatureTest");
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					currentProjectName(editor));
			wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_KEEPRUNNING,
					false);
			wc.setAttribute(
					JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, "");
			wc.setAttribute(
					JUnitLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND,
					TestKindRegistry.JUNIT4_TEST_KIND_ID);
			JUnitMigrationDelegate.mapResources(wc);
			return wc;
		} catch (final CoreException e) {
			ExceptionHandler
					.handle(e,
							getParentShell(),
							SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_dialog_title,
							SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_error_launch);
			return null;
		}
	}

	private String currentProjectName(final org.eclipse.ui.IEditorPart editor) {
		final IResource resource = (IResource) editor.getEditorInput()
				.getAdapter(IResource.class);
		return resource.getProject().getName();
	}

	private void noFeatureFoundDialog() {
		MessageDialog
				.openInformation(
						getParentShell(),
						SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_dialog_title,
						SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_message_notests);
	}

	private Shell getParentShell() {
		return FeatureRunnerPlugin.instance().getActiveShell();
	}

	private ILaunchManager launchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
}
