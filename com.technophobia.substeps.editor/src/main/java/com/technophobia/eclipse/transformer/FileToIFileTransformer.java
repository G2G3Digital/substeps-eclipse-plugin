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
