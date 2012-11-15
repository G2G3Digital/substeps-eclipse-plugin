package com.technophobia.eclipse.transformer;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.technophobia.substeps.supplier.Transformer;

public class FileToIFileTransformer implements Transformer<File, IFile> {

    private final IProject project;


    public FileToIFileTransformer(final IProject project) {
        this.project = project;
    }


    @Override
    public IFile from(final File from) {

        final IPath projPath = project.getLocation();

        final File projectFile = projPath.toFile();

        final String projectAbsolute = projectFile.getAbsolutePath();
        final String fileAbsolute = from.getAbsolutePath();

        if (!fileAbsolute.startsWith(projectAbsolute)) {
            return null;
        }

        final String fileRelative = fileAbsolute.substring(projectAbsolute.length());

        return project.getFile(new Path(fileRelative));
    }

}
