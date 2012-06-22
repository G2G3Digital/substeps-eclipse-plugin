package com.technophobia.substeps.junit.ui;

import org.eclipse.osgi.util.NLS;

public class SubstepsFeatureMessages {
    private static final String BUNDLE_NAME = "com.technophobia.substeps.junit.ui.SubstepsFeatureMessages";

    public static String SubstepsFeatureLaunchShortcut_dialog_title;
    public static String SubstepsFeatureLaunchShortcut_message_notests;
    public static String SubstepsFeatureLaunchShortcut_error_launch;

    public static String SubstepsFeature_choose_config_title;
    public static String SubstepsFeature_choose_config_message;

    public static String SubstepsFeatureTestRunnerViewPart_titleToolTip;

    public static String SubstepsFeatureTestRunnerViewPart_jobName;
    public static String SubstepsFeatureTestRunnerViewPart_wrapperJobName;

    public static String SubstepsFeatureTestRunnerViewPart_message_started;
    public static String SubstepsFeatureTestRunnerViewPart_message_stopped;
    public static String SubstepsFeatureTestRunnerViewPart_message_terminated;

    public static String SubstepsFeatureTestRunnerViewPart_rerunaction_label;
    public static String SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label;

    public static String SubstepsFeatureTestRunnerViewPart_terminate_title;
    public static String SubstepsFeatureTestRunnerViewPart_terminate_message;

    public static String SubstepsFeatureTestRunnerViewPart_Launching;

    public static String SubstepsFeatureTestRunnerViewPart_message_stopping;

    public static String SubstepsFeatureTestRunnerViewPart_stopaction_text;

    public static String SubstepsFeatureTestRunnerViewPart_configName;

    public static String SubstepsFeatureTestRunnerViewPart_rerunFailedFirstLaunchConfigName;
    public static String SubstepsFeatureTestRunnerViewPart_error_cannotrerun;

    public static String SubstepsFeatureTestRunnerViewPart_message_finish;
    public static String SubstepsFeatureTestRunnerViewPart_activate_on_failure_only;

    public static String SubstepsFeatureTestRunnerViewPart_cannotrerun_title;
    public static String SubstepsFeatureTestRunnerViewPart_cannotrerurn_message;

    public static String SubstepsFeatureTestRunnerViewPart_clear_history_label;
    public static String SubstepsFeatureTestRunnerViewPart_test_run_history;
    public static String SubstepsFeatureTestRunnerViewPart_history;

    public static String SubstepsFeatureTestRunnerViewPart_test_runs;
    public static String SubstepsFeatureTestRunnerViewPart_select_test_run;
    public static String SubstepsFeatureTestRunnerViewPart_testName_startTime;
    public static String SubstepsFeatureTestRunnerViewPart_max_remembered;
    public static String SubstepsFeatureTestRunnerViewPart_show_execution_time;
    public static String SubstepsFeatureTestRunnerViewPart_show_failures_only;

    public static String SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_name;
    public static String SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_title;

    public static String SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_name;
    public static String SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_title;
    public static String SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_invalid_url;
    public static String SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_import_from_url;
    public static String SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_url;

    public static String SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_label;
    public static String SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_cannotpaste_title;
    public static String SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_cannotpaste_message;

    public static String SubstepsFeatureTestRunnerViewPart_hierarchical_layout;
    public static String SubstepsFeatureTestRunnerViewPart_toggle_horizontal_label;
    public static String SubstepsFeatureTestRunnerViewPart_toggle_vertical_label;
    public static String SubstepsFeatureTestRunnerViewPart_toggle_automatic_label;
    public static String SubstepsFeatureTestRunnerViewPart_label_failure;
    public static String SubstepsFeatureTestRunnerViewPart_layout_menu;

    public static String CompareResultsAction_label;
    public static String CompareResultsAction_description;
    public static String CompareResultsAction_tooltip;

    public static String CopyTrace_action_label;
    public static String CopyTraceAction_problem;
    public static String CopyTraceAction_clipboard_busy;

    public static String EnableStackFilterAction_action_label;
    public static String EnableStackFilterAction_action_description;
    public static String EnableStackFilterAction_action_tooltip;

    public static String ExpandAllAction_text;
    public static String ExpandAllAction_tooltip;

    public static String RerunAction_label_run;
    public static String RerunAction_label_debug;
    public static String RerunAction_label_rerun;

    public static String ScrollLockAction_action_label;
    public static String ScrollLockAction_action_tooltip;

    public static String ShowNextFailureAction_label;
    public static String ShowNextFailureAction_tooltip;
    public static String ShowPreviousFailureAction_label;
    public static String ShowPreviousFailureAction_tooltip;

    public static String TestSessionLabelProvider_testName_JUnitVersion;
    public static String TestSessionLabelProvider_testMethodName_className;
    public static String TestSessionLabelProvider_testName_elapsedTimeInSeconds;

    static {
        NLS.initializeMessages(BUNDLE_NAME, SubstepsFeatureMessages.class);
    }


    private SubstepsFeatureMessages() {
        // Do not instantiate
    }
}
