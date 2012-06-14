package com.technophobia.substeps.junit.launcher;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

import com.technophobia.eclipse.launcher.config.FindExistingOrNewLaunchConfigFactory;
import com.technophobia.eclipse.launcher.config.LaunchConfigurationFactory;
import com.technophobia.eclipse.launcher.config.LaunchConfigurationWorkingCopyFactory;
import com.technophobia.eclipse.launcher.exception.DialogExceptionReporter;
import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.eclipse.transformer.ProjectToJavaProjectTransformer;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.launcher.config.JunitResourceMappingDecorator;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyDecorator;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyFactory;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class SubstepsFeatureLaunchShortcut extends JUnitLaunchShortcut {

    public static final String ATTR_FEATURE_FILE = "com.technophobia.substeps.junit.featureFile";

    private final LaunchConfigurationWorkingCopyFactory workingCopyFactory;
    private final LaunchConfigurationFactory launchConfigurationFactory;


    /**
     * Default Constructor called by eclipse's Extension framework
     */
    public SubstepsFeatureLaunchShortcut() {
        final ExceptionReporter exceptionReporter = exceptionReporter();
        final ILaunchManager launchManager = launchManager();

        this.workingCopyFactory = new SubstepsLaunchConfigWorkingCopyFactory(launchManager,
                workingCopyDecorators(exceptionReporter), exceptionReporter);
        this.launchConfigurationFactory = new FindExistingOrNewLaunchConfigFactory(new String[] { ATTR_FEATURE_FILE },
                launchManager, exceptionReporter);
    }


    public SubstepsFeatureLaunchShortcut(final LaunchConfigurationWorkingCopyFactory workingCopyFactory,
            final LaunchConfigurationFactory launchConfigurationFactory) {
        this.workingCopyFactory = workingCopyFactory;
        this.launchConfigurationFactory = launchConfigurationFactory;
    }


    @Override
    public void launch(final org.eclipse.ui.IEditorPart editor, final String mode) {
        final ILaunchConfigurationWorkingCopy workingCopy = workingCopyFactory.create(editor.getTitle(),
                (IResource) editor.getEditorInput().getAdapter(IResource.class));
        final ILaunchConfiguration config = launchConfigurationFactory.create(workingCopy);
        DebugUITools.launch(config, mode);
    }


    private ExceptionReporter exceptionReporter() {
        return new DialogExceptionReporter(FeatureRunnerPlugin.instance().getActiveShell(),
                SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_dialog_title,
                SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_message_notests);
    }


    @SuppressWarnings("unchecked")
    private Collection<Decorator<ILaunchConfigurationWorkingCopy, IResource>> workingCopyDecorators(
            final ExceptionReporter exceptionReporter) {
        return Arrays.asList(new SubstepsLaunchConfigWorkingCopyDecorator(new ProjectToJavaProjectTransformer(),
                exceptionReporter), new JunitResourceMappingDecorator(exceptionReporter));
    }


    private ILaunchManager launchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }
}