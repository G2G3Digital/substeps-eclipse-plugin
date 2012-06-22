package com.technophobia.substeps.junit.action;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ActivateOnErrorAction extends Action {
    private final Notifier<Boolean> showOnErrorOnlyNotifier;


    public ActivateOnErrorAction(final boolean initialValue, final Notifier<Boolean> showOnErrorOnlyNotifier) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_activate_on_failure_only, IAction.AS_CHECK_BOX);
        this.showOnErrorOnlyNotifier = showOnErrorOnlyNotifier;
        //setImageDescriptor(JUnitPlugin.getImageDescriptor("obj16/failures.gif")); //$NON-NLS-1$

        update(initialValue);
    }


    public void update(final boolean value) {
        setChecked(value);
    }


    @Override
    public void run() {
        final boolean checked = isChecked();
        showOnErrorOnlyNotifier.notify(checked);
        InstanceScope.INSTANCE.getNode(JUnitCorePlugin.CORE_PLUGIN_ID).putBoolean(
                JUnitPreferencesConstants.SHOW_ON_ERROR_ONLY, checked);
    }
}
