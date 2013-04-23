/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.junit.launcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class SubstepJarProvider {

    public List<String> allSubstepJars(){
        final List<String> substepsEntries = new ArrayList<String>();

        substepsEntries.add(entryString(new SubstepsRuntimeClasspathEntry(FeatureRunnerPlugin.PLUGIN_ID, "target/classes")));
        substepsEntries.add(apiJar());
        substepsEntries.add(coreJar());
        substepsEntries.addAll(junitRunnerJars());
        return substepsEntries;
    }
    
    public String apiJar(){
    	return entryStringForBundle("substeps-core-api", FeatureRunnerPlugin.SUBSTEPS_CORE_VERSION);
    }
    
    public String coreJar(){
    	return entryStringForBundle("substeps-core", FeatureRunnerPlugin.SUBSTEPS_CORE_VERSION);
    }
    
    public List<String> junitRunnerJars(){
    	return Arrays.asList(//
    			entryStringForBundle("substeps-junit-runner", FeatureRunnerPlugin.SUBSTEPS_JUNIT_VERSION), //
    			entryStringForBundle("substeps-runner-common", FeatureRunnerPlugin.SUBSTEPS_JUNIT_VERSION) //
    		);
    }

    private String entryStringForBundle(String jarName, String version) {
    	return entryString(new SubstepsRuntimeClasspathEntry(FeatureRunnerPlugin.DEPENDENCY_BUNDLE_PLUGIN_ID, createJarName(jarName, version)));
    }


    private String createJarName(String name, String substepsVersion) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(name);
    	sb.append("-");
    	sb.append(substepsVersion);
    	sb.append(".jar");
    	return sb.toString();
	}


	

    private String entryString(final SubstepsRuntimeClasspathEntry entry) {
        final Bundle bundle = FeatureRunnerPlugin.instance().getBundle(entry.getPluginId());
        URL url = bundle.getEntry(entry.getPluginRelativePath());

        if (url != null){
        	return toFile(url);
        }
        throw new IllegalStateException("Could not get bundle url for classpath entry "+entry);
    }


	private String toFile(URL url) {
		try {
			return FileLocator.toFileURL(url).getFile();
		} catch (IOException ex) {
			throw new IllegalStateException("Could not get url "+url+" as file", ex);
		}
	}
}
