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
package com.technophobia.substeps.junit.launcher.tab.component;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.technophobia.eclipse.transformer.Callback;
import com.technophobia.substeps.junit.launcher.tab.TabComponent;
import com.technophobia.substeps.supplier.Supplier;

public abstract class AbstractTabComponent implements TabComponent {

    private final Callback onChangeCallback;
    private final Collection<TabComponent> dependentTabComponents;
    private final Supplier<IProject> projectSupplier;


    public AbstractTabComponent(final Callback onChangeCallback, final Supplier<IProject> projectSupplier) {
        this.onChangeCallback = onChangeCallback;
        this.projectSupplier = projectSupplier;
        this.dependentTabComponents = new ArrayList<TabComponent>();
    }


    public void addDependentTabComponent(final TabComponent tabComponent) {
        this.dependentTabComponents.add(tabComponent);
    }


    protected abstract boolean isValid();


    protected void onChange() {
        onChangeCallback.callback();

        final boolean isValid = isValid();

        for (final TabComponent dependentTabs : dependentTabComponents) {
            if (isValid) {
                dependentTabs.enableControls();
            } else {
                dependentTabs.disableControls();
            }
        }
    }


    protected IProject project() {
        return projectSupplier.get();
    }


    /**
     * Convert a resource to its os-specific project localised path string
     * 
     * @param resource
     *            to be localised
     * @return localised path
     */
    protected String projectLocalisedPathFor(final IResource resource) {
        return resource.getFullPath().removeFirstSegments(1).toOSString();
    }


    /*
     * Convenience method to get the workspace root.
     */
    protected IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }
}
