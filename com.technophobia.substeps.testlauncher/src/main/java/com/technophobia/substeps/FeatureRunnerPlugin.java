package com.technophobia.substeps;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FeatureRunnerPlugin extends AbstractUIPlugin implements
		BundleActivator {

	private static final String PLUGIN_ID = "com.technophobia.substeps.editor";

	private static FeatureRunnerPlugin pluginInstance;

	private ILog log;

	public FeatureRunnerPlugin() {
		super();
		FeatureRunnerPlugin.pluginInstance = this;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		log = Platform.getLog(bundleContext.getBundle());
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		log = null;
	}

	public Shell getActiveShell() {
		return getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static void log(final int status, final String message) {
		instance().log.log(new Status(status, PLUGIN_ID, message));
	}

	public static FeatureRunnerPlugin instance() {
		return pluginInstance;
	}
}
