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

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.supplier.Transformer;

public class DefaultSubstepsLocationFinder implements Transformer<IProject, String> {

    private static final String[] DEFAULT_SUBSTEPS_FOLDER_LOCATIONS = { "substeps", "src/main/resources/substeps",
            "src/test/resources/substeps" };


    @Override
    public String from(final IProject project) {
        for (final String defaultSubstepsFolder : DEFAULT_SUBSTEPS_FOLDER_LOCATIONS) {
            if (project.getFolder(defaultSubstepsFolder).exists()) {
                return defaultSubstepsFolder;
            }
        }

        return null;
    }

}
