/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * BundleActivator/general bundle aware class for managing things such as
 * logging, requesting the bundle context etc
 * 
 * @author sforbes
 * 
 */
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
            resourceBundle = ResourceBundle.getBundle("com.technophobia.substeps.FeatureEditorResources");
        } catch (final MissingResourceException x) {
            resourceBundle = null;
        }
    }


    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        resourceBundle = null;
        log = null;
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
