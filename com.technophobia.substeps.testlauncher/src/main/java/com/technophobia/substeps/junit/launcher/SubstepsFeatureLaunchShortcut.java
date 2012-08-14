package com.technophobia.substeps.junit.launcher;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

import com.technophobia.eclipse.launcher.config.DialogConfigSelector;
import com.technophobia.eclipse.launcher.config.FindExistingOrNewLaunchConfigFactory;
import com.technophobia.eclipse.launcher.config.LaunchConfigurationFactory;
import com.technophobia.eclipse.launcher.config.LaunchConfigurationWorkingCopyFactory;
import com.technophobia.eclipse.launcher.config.WorkingCopyLaunchConfigLocator;
import com.technophobia.eclipse.launcher.exception.DialogExceptionReporter;
import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.eclipse.transformer.Locator;
import com.technophobia.eclipse.transformer.Transformers;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.launcher.config.ResourceMappingDecorator;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyDecorator;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyFactory;
import com.technophobia.substeps.junit.launcher.model.SubstepsLaunchModelFactory;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class SubstepsFeatureLaunchShortcut implements ILaunchShortcut2 {

    public static final String ATTR_FEATURE_FILE = "com.technophobia.substeps.junit.featureFile";

    private final LaunchConfigurationWorkingCopyFactory workingCopyFactory;
    private final LaunchConfigurationFactory launchConfigurationFactory;

    private final Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> configLocator;


    /**
     * Default Constructor called by eclipse's Extension framework
     */
    public SubstepsFeatureLaunchShortcut() {
        final ExceptionReporter exceptionReporter = exceptionReporter();
        final ILaunchManager launchManager = launchManager();
        this.configLocator = configLocator(launchManager, exceptionReporter);

        this.workingCopyFactory = new SubstepsLaunchConfigWorkingCopyFactory(launchManager,
                workingCopyDecorators(exceptionReporter), exceptionReporter);
        this.launchConfigurationFactory = new FindExistingOrNewLaunchConfigFactory(configLocator, exceptionReporter);
    }


    public SubstepsFeatureLaunchShortcut(final LaunchConfigurationWorkingCopyFactory workingCopyFactory,
            final LaunchConfigurationFactory launchConfigurationFactory,
            final Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> configLocator) {
        this.workingCopyFactory = workingCopyFactory;
        this.launchConfigurationFactory = launchConfigurationFactory;
        this.configLocator = configLocator;
    }


    @Override
    public void launch(final org.eclipse.ui.IEditorPart editor, final String mode) {
        final ILaunchConfigurationWorkingCopy workingCopy = workingCopyFactory.create(editor.getTitle(),
                (IResource) editor.getEditorInput().getAdapter(IResource.class));
        final ILaunchConfiguration config = launchConfigurationFactory.create(workingCopy);
        DebugUITools.launch(config, mode);
    }


    @Override
    public void launch(final ISelection selection, final String mode) {
        // TODO: Find all scenarios in selection, and run only them
        final IFile file = Transformers.selectionToFileOrNull(selection);
        if (file != null) {
            final ILaunchConfigurationWorkingCopy workingCopy = workingCopyFactory.create(file.getName(), file);
            final ILaunchConfiguration config = launchConfigurationFactory.create(workingCopy);
            DebugUITools.launch(config, mode);
        }
    }


    @Override
    public ILaunchConfiguration[] getLaunchConfigurations(final ISelection selection) {
        final IFile file = Transformers.selectionToFileOrNull(selection);
        if (file != null) {
            final ILaunchConfigurationWorkingCopy workingCopy = workingCopyFactory.create(file.getName(), file);
            final Collection<ILaunchConfiguration> allConfigs = configLocator.all(workingCopy);
            return allConfigs.toArray(new ILaunchConfiguration[allConfigs.size()]);
        }
        return new ILaunchConfiguration[0];
    }


    @Override
    public ILaunchConfiguration[] getLaunchConfigurations(final IEditorPart editorpart) {
        final ILaunchConfigurationWorkingCopy workingCopy = workingCopyFactory.create(editorpart.getTitle(),
                (IResource) editorpart.getEditorInput().getAdapter(IResource.class));
        final Collection<ILaunchConfiguration> allConfigs = configLocator.all(workingCopy);
        return allConfigs.toArray(new ILaunchConfiguration[allConfigs.size()]);
    }


    @Override
    public IResource getLaunchableResource(final ISelection selection) {
        return Transformers.selectionToFileOrNull(selection);
    }


    @Override
    public IResource getLaunchableResource(final IEditorPart editorpart) {
        return Transformers.editorToResource(editorpart);
    }


    private ExceptionReporter exceptionReporter() {
        return new DialogExceptionReporter(shell(), SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_dialog_title,
                SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_message_notests);
    }


    private Shell shell() {
        return FeatureRunnerPlugin.instance().getActiveShell();
    }


    @SuppressWarnings("unchecked")
    private Collection<Decorator<ILaunchConfigurationWorkingCopy, IResource>> workingCopyDecorators(
            final ExceptionReporter exceptionReporter) {
        return Arrays.asList(new SubstepsLaunchConfigWorkingCopyDecorator(new SubstepsLaunchModelFactory(
                new DefaultSubstepsLocationFinder())), new ResourceMappingDecorator(exceptionReporter));
    }


    private ILaunchManager launchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }


    private Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> configLocator(
            final ILaunchManager launchManager, final ExceptionReporter exceptionReporter) {
        return new WorkingCopyLaunchConfigLocator(new String[] { ATTR_FEATURE_FILE }, launchManager,
                new DialogConfigSelector(shell(), SubstepsFeatureMessages.SubstepsFeature_choose_config_title,
                        SubstepsFeatureMessages.SubstepsFeature_choose_config_title), exceptionReporter);
    }
}