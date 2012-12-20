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
package com.technophobia.substeps.editor.outline.substeps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.text.Position;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.model.SubstepsDefinitionElement;
import com.technophobia.substeps.editor.outline.model.SubstepsRootElement;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.PatternMap;
import com.technophobia.substeps.model.Step;
import com.technophobia.substeps.runner.syntax.SubStepDefinitionParser;
import com.technophobia.substeps.supplier.Transformer;
import com.technophobia.substeps.syntax.NullSyntaxErrorReporter;

public class FileToSubstepDefinitionElementTransformer implements Transformer<ProjectFile, AbstractModelElement> {

    private final Transformer<Integer, Position> lineNumberToPositionTransformer;


    public FileToSubstepDefinitionElementTransformer(
            final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        this.lineNumberToPositionTransformer = lineNumberToPositionTransformer;
    }


    @Override
    public AbstractModelElement from(final ProjectFile from) {
        final SubStepDefinitionParser parser = new SubStepDefinitionParser(new NullSyntaxErrorReporter());
        final PatternMap<ParentStep> stepMap = parser.loadSubSteps(from.getFile());

        final SubstepsRootElement root = new SubstepsRootElement("Substep", new Position(0));

        final List<SubstepsDefinitionElement> substeps = sortedSubstepDefinitions(stepMap);

        for (final SubstepsDefinitionElement substep : substeps) {
            root.addSubstepsDefinition(substep);
        }

        return root;
    }


    private List<SubstepsDefinitionElement> sortedSubstepDefinitions(final PatternMap<ParentStep> stepMap) {
        final List<SubstepsDefinitionElement> substeps = new ArrayList<SubstepsDefinitionElement>();
        for (final Pattern pattern : stepMap.keySet()) {
            substeps.add(createSubstepDefinition(stepMap, pattern));
        }
        Collections.sort(substeps);
        return substeps;
    }


    private SubstepsDefinitionElement createSubstepDefinition(final PatternMap<ParentStep> stepMap,
            final Pattern pattern) {
        final ParentStep step = stepMap.getValueForPattern(pattern.pattern());
        final Position lineNumber = lineNumberToPositionTransformer.from(Integer.valueOf(step.getSourceLineNumber()));
        final SubstepsDefinitionElement substepDefinition = new SubstepsDefinitionElement(step.getParent().getLine(),
                lineNumber);

        final List<Step> steps = step.getSteps();
        addStepsTo(substepDefinition, steps != null ? steps : Collections.<Step> emptyList());
        return substepDefinition;
    }


    private void addStepsTo(final SubstepsDefinitionElement substepDefinition, final List<Step> steps) {
        for (final Step step : steps) {
            final Position position = lineNumberToPositionTransformer.from(Integer.valueOf(step.getSourceLineNumber()));
            substepDefinition.addStep(new StepElement(step.getLine(), position));
        }
    }
}
