package com.technophobia.substeps.junit.ui;

import org.eclipse.osgi.util.NLS;

public class SubstepsFeatureMessages {
	private static final String BUNDLE_NAME = "com.technophobia.substeps.junit.ui.SubstepsFeatureMessages";

	public static String SubstepsFeatureLaunchShortcut_dialog_title;
	public static String SubstepsFeatureLaunchShortcut_message_notests;
	public static String SubstepsFeatureLaunchShortcut_error_launch;

	static {
		NLS.initializeMessages(BUNDLE_NAME, SubstepsFeatureMessages.class);
	}

	private SubstepsFeatureMessages() {
		// Do not instantiate
	}
}
