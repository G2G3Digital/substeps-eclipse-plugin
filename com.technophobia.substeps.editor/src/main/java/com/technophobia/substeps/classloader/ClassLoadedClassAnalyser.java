package com.technophobia.substeps.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IStatus;

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
            FeatureEditorPlugin.instance().log(IStatus.ERROR,
                    "Could not invoke method " + methodName + " on object " + ob);
            return null;
        }
    }
}
