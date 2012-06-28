package com.technophobia.substeps.preferences;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class PreferencesConstants {

    /**
     * Boolean preference controlling whether the Substeps view should be shown
     * on errors only.
     */
    public static final String SHOW_ON_ERROR_ONLY = FeatureRunnerPlugin.PLUGIN_ID + ".show_on_error"; //$NON-NLS-1$
    public static final String MAX_TEST_RUNS = FeatureRunnerPlugin.PLUGIN_ID + ".max_test_runs";;


    public static boolean getFilterStack() {
        return false;
    }


    public static String[] getFilterPatterns() {
        // TODO Auto-generated method stub
        return null;
    }


    public static boolean getShowInAllViews() {
        // TODO Auto-generated method stub
        return false;
    }


    public static void setFilterStack(final boolean checked) {
        // TODO Auto-generated method stub

    }
}
