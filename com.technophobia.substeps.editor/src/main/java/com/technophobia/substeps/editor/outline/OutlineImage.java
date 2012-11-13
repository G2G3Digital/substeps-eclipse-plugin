package com.technophobia.substeps.editor.outline;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.BackgroundElement;
import com.technophobia.substeps.editor.outline.model.ExampleElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.ScenarioElement;
import com.technophobia.substeps.editor.outline.model.ScenarioOutlineElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.model.SubstepsDefinitionElement;

public enum OutlineImage {

    Background(BackgroundElement.class, "outline-background.gif"), //
    ExampleRow(ExampleRowElement.class, "outline-example-row.gif"), //
    Example(ExampleElement.class, "outline-example.gif"), //
    Scenario(ScenarioElement.class, "outline-scenario.gif"), //
    ScenarioOutline(ScenarioOutlineElement.class, "outline-scenario-outline.gif"), //
    Step(StepElement.class, "outline-step.gif"), //
    SubstepDefinition(SubstepsDefinitionElement.class, "outline-scenario.gif");

    private final Class<? extends AbstractModelElement> modelClass;
    private final String imagePath;


    private OutlineImage(final Class<? extends AbstractModelElement> modelClass, final String imagePath) {
        this.modelClass = modelClass;
        this.imagePath = imagePath;
    }


    public String imagePath() {
        return imagePath;
    }


    public static OutlineImage findForModel(final AbstractModelElement model) {
        for (final OutlineImage outlineImage : values()) {
            if (model.getClass().equals(outlineImage.modelClass)) {
                return outlineImage;
            }
        }
        return null;
    }
}
