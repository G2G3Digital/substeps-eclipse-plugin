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
package com.technophobia.substeps.runner;

public interface ITestLoader {
    /**
     * @param testClasses
     *            classes to be run
     * @param testName
     *            individual method to be run
     * @param failureNames
     *            may want to run these first, since they failed
     * @param listener
     *            to be notified if tests could not be loaded
     * @return the loaded test references
     */
    ITestReference[] loadTests(Class<?>[] testClasses, String testName, String[] failureNames, RemoteTestRunner listener);
}
