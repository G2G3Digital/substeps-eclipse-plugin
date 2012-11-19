package com.technophobia.substeps.editor.outline.substeps;

import java.io.File;

import org.eclipse.core.resources.IProject;

public class ProjectFile {

    private final IProject project;
    private final File file;


    public ProjectFile(final IProject project, final File file) {
        this.project = project;
        this.file = file;
    }


    public IProject getProject() {
        return project;
    }


    public File getFile() {
        return file;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.getAbsolutePath().hashCode());
        result = prime * result + ((project == null) ? 0 : project.getName().hashCode());
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
        final ProjectFile other = (ProjectFile) obj;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.getAbsolutePath().equals(other.file.getAbsolutePath()))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.getName().equals(other.project.getName()))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Project File: project" + project.getName() + ", file " + file.getAbsolutePath();
    }
}
