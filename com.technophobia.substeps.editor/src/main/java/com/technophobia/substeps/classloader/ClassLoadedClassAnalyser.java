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
package com.technophobia.substeps.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.model.SubSteps.AdditionalStepImplementations;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.runner.syntax.ClassAnalyser;

/**
 * Subclass of ClassAnalyser that handles classes that have been loaded in a
 * different Classloader.
 * 
 * @author sforbes
 * 
 */
public class ClassLoadedClassAnalyser extends ClassAnalyser {

    private final ClassLoader classLoader;


    public ClassLoadedClassAnalyser(final ClassLoader classLoader) {
        super();
        this.classLoader = classLoader;
    }


    @Override
    protected boolean isStepMethod(final Method m) {
        return m.isAnnotationPresent(getLoadedClassFor(Step.class));
    }


    @Override
    protected boolean hasAdditionalStepsAnnotation(final Class<?> loadedClass) {
        return loadedClass.isAnnotationPresent(getLoadedClassFor(AdditionalStepImplementations.class));
    }


    @Override
    protected Class<?>[] getAdditionalStepClasses(final Class<?> loadedClass) {
        final Class<AdditionalStepImplementations> loadedAnnotationClass = getLoadedClassFor(AdditionalStepImplementations.class);
        final Object annotation = loadedClass.getAnnotation(loadedAnnotationClass);
        return valueAttributeFromObject(annotation, "value");
    }


    @Override
    protected String stepValueFrom(final Method m) {
        final Class<Step> loadedAnnotationClass = getLoadedClassFor(Step.class);
        final Object annotation = m.getAnnotation(loadedAnnotationClass);
        return valueAttributeFromObject(annotation, "value");
    }


    /**
     * Use the classloader to reload clazz
     * 
     * @param clazz
     *            The class to be reloaded in the classloader
     * @return The reloaded class
     */
    @SuppressWarnings("unchecked")
    private <T extends Annotation> Class<T> getLoadedClassFor(final Class<T> clazz) {
        try {
            return (Class<T>) classLoader.loadClass(clazz.getName());
        } catch (final ClassNotFoundException ex) {
            throw new IllegalStateException("Could not find class " + clazz, ex);
        }
    }


    /**
     * Reflectively gets property from object
     * 
     * @param ob
     *            The object containing the method
     * @param methodName
     *            The method to be looked up
     * @return value of the object method
     */
    @SuppressWarnings("unchecked")
    private <T> T valueAttributeFromObject(final Object ob, final String methodName) {
        try {
            return (T) ob.getClass().getMethod(methodName).invoke(ob);
        } catch (final Exception e) {
            FeatureEditorPlugin.instance().error("Could not invoke method " + methodName + " on object " + ob);
            return null;
        }
    }
}
