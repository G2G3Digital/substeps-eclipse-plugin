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

public class SubstepsRuntimeClasspathEntry {
    private final String pluginId;

    private final String pluginRelativePath;


    public SubstepsRuntimeClasspathEntry(final String pluginId, final String relativePath) {
        this.pluginId = pluginId;
        this.pluginRelativePath = relativePath;
    }


    public String getPluginId() {
        return pluginId;
    }


    public String getPluginRelativePath() {
        return pluginRelativePath;
    }


    @Override
    public String toString() {
        return "ClasspathEntry(" + pluginId + "/" + pluginRelativePath + ")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pluginId == null) ? 0 : pluginId.hashCode());
        result = prime * result + ((pluginRelativePath == null) ? 0 : pluginRelativePath.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SubstepsRuntimeClasspathEntry other = (SubstepsRuntimeClasspathEntry) obj;
        if (pluginId == null) {
            if (other.pluginId != null)
                return false;
        } else if (!pluginId.equals(other.pluginId))
            return false;
        if (pluginRelativePath == null) {
            if (other.pluginRelativePath != null)
                return false;
        } else if (!pluginRelativePath.equals(other.pluginRelativePath))
            return false;
        return true;
    }
}
