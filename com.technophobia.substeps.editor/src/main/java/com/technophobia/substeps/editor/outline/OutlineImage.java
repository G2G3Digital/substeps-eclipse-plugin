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
