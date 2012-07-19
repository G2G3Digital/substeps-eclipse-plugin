package com.technophobia.substeps.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class PreferencesConstants {

    /**
     * Boolean preference controlling whether the Substeps view should be shown
     * on errors only.
     */
    public static final String SHOW_ON_ERROR_ONLY = FeatureRunnerPlugin.PLUGIN_ID + ".show_on_error";

    /**
     * Boolean preference controlling whether the failure stack should be
     * filtered.
     */
    public static final String DO_FILTER_STACK = FeatureRunnerPlugin.PLUGIN_ID + ".do_filter_stack";

    public static final String MAX_TEST_RUNS = FeatureRunnerPlugin.PLUGIN_ID + ".max_test_runs";

    public static final String PREF_ACTIVE_FILTERS_LIST = FeatureRunnerPlugin.PLUGIN_ID + ".active_filters";

    private static final String DEFAULT_FILTER_PATTERNS = defaultFilterPatterns();


    public static boolean getFilterStack() {
        return Platform.getPreferencesService().getBoolean(FeatureRunnerPlugin.PLUGIN_ID, DO_FILTER_STACK, true, null);
    }


    private static String defaultFilterPatterns() {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.technophobia.substeps.runner.*,");
        sb.append("org.junit.*,");
        sb.append("java.lang.reflect.Method.invoke,");
        sb.append("sun.reflect.*");
        return sb.toString();
    }


    public static String[] getFilterPatterns() {
        return Platform.getPreferencesService()
                .getString(FeatureRunnerPlugin.PLUGIN_ID, PREF_ACTIVE_FILTERS_LIST, DEFAULT_FILTER_PATTERNS, null)
                .split(",");
    }


    public static boolean getShowInAllViews() {
        // TODO Auto-generated method stub
        return false;
    }


    public static void setFilterStack(final boolean checked) {
        InstanceScope.INSTANCE.getNode(FeatureRunnerPlugin.PLUGIN_ID).putBoolean(DO_FILTER_STACK, checked);
    }
}
