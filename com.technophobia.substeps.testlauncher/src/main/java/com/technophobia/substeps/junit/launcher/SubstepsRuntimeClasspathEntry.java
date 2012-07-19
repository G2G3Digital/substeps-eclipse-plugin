package com.technophobia.substeps.junit.launcher;

public class SubstepsRuntimeClasspathEntry {
    private final String pluginId;

    private final String pluginRelativePath;


    public SubstepsRuntimeClasspathEntry(final String pluginId, final String jarFile) {
        this.pluginId = pluginId;
        this.pluginRelativePath = jarFile;
    }


    public String getPluginId() {
        return pluginId;
    }


    public String getPluginRelativePath() {
        return pluginRelativePath;
    }


    public SubstepsRuntimeClasspathEntry developmentModeEntry() {
        return new SubstepsRuntimeClasspathEntry(getPluginId(), "target/classes"); //$NON-NLS-1$
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
