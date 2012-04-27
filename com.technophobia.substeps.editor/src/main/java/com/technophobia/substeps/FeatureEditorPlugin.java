package com.technophobia.substeps;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FeatureEditorPlugin implements BundleActivator {

	private static final String PLUGIN_ID = "com.technophobia.substeps.editor";

	private static FeatureEditorPlugin pluginInstance;

	private BundleContext context;
	private ResourceBundle resourceBundle;
	private ILog log;

	public FeatureEditorPlugin() {
		super();
		FeatureEditorPlugin.pluginInstance = this;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		context = bundleContext;
		log = Platform.getLog(bundleContext.getBundle());
		try {
			resourceBundle = ResourceBundle
					.getBundle("com.technophobia.substeps.FeatureEditorResources");
		} catch (final MissingResourceException x) {
			resourceBundle = null;
		}
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		resourceBundle = null;
		log = null;
		resourceBundle = null;
	}

	public BundleContext getBundleContext() {
		return context;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static void log(final int status, final String message) {
		instance().log.log(new Status(status, PLUGIN_ID, message));
	}

	public static FeatureEditorPlugin instance() {
		return pluginInstance;
	}
}
