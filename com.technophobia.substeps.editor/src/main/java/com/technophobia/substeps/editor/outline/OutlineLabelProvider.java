package com.technophobia.substeps.editor.outline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.BackgroundElement;
import com.technophobia.substeps.editor.outline.model.ExampleElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.editor.outline.model.ScenarioElement;
import com.technophobia.substeps.editor.outline.model.ScenarioOutlineElement;
import com.technophobia.substeps.editor.outline.model.StepElement;

public class OutlineLabelProvider implements ILabelProvider {

    private static final Display DISPLAY = Display.getDefault();

    private static final Image BACKGROUND_IMAGE = loadImage("outline-background.gif");
    private static final Image EXAMPLE_ROW_IMAGE = loadImage("outline-example-row.gif");
    private static final Image EXAMPLE_IMAGE = loadImage("outline-example.gif");
    private static final Image FEATURE_IMAGE = loadImage("outline-feature.gif");
    private static final Image SCENARIO_IMAGE = loadImage("outline-scenario.gif");
    private static final Image SCENARIO_OUTLINE_IMAGE = loadImage("outline-scenario-outline.gif");
    private static final Image STEP_IMAGE = loadImage("outline-step.gif");

    private final Map<Class<?>, Image> imageMap;


    public OutlineLabelProvider() {
        super();
        this.imageMap = loadImageMap();
    }


    @Override
    public Image getImage(final Object element) {
        return imageMap.get(element.getClass());
    }


    @Override
    public String getText(final Object element) {
        if (element instanceof AbstractModelElement) {
            final AbstractModelElement substepsElement = (AbstractModelElement) element;
            return substepsElement.getText();
        }
        return null;
    }


    @Override
    public void addListener(final ILabelProviderListener listener) {
        // No-op
    }


    @Override
    public void dispose() {
        // No-op
    }


    @Override
    public boolean isLabelProperty(final Object element, final String property) {
        return false;
    }


    @Override
    public void removeListener(final ILabelProviderListener listener) {
        // No-op
    }


    private Map<Class<?>, Image> loadImageMap() {
        final Map<Class<?>, Image> map = new HashMap<Class<?>, Image>();
        map.put(BackgroundElement.class, BACKGROUND_IMAGE);
        map.put(ExampleElement.class, EXAMPLE_IMAGE);
        map.put(ExampleRowElement.class, EXAMPLE_ROW_IMAGE);
        map.put(FeatureElement.class, FEATURE_IMAGE);
        map.put(ScenarioElement.class, SCENARIO_IMAGE);
        map.put(ScenarioOutlineElement.class, SCENARIO_OUTLINE_IMAGE);
        map.put(StepElement.class, STEP_IMAGE);
        return Collections.unmodifiableMap(map);
    }


    private static Image loadImage(final String filename) {
        return new Image(DISPLAY, OutlineLabelProvider.class.getResourceAsStream("/icons/" + filename));
    }
}