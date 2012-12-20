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
package com.technophobia.substeps.editor.outline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.Position;

public class FeatureElement extends AbstractModelElement {

    private BackgroundElement background;
    private final Collection<ScenarioElement> scenarios;
    private final Collection<ScenarioOutlineElement> scenarioOutlines;


    public FeatureElement(final String text, final Position position) {
        super(text, position);

        this.background = null;
        this.scenarios = new ArrayList<ScenarioElement>();
        this.scenarioOutlines = new ArrayList<ScenarioOutlineElement>();
    }


    public void setBackground(final BackgroundElement background) {
        this.background = background;
        this.background.setParent(this);
    }


    public void addScenario(final ScenarioElement scenario) {
        scenario.setParent(this);
        this.scenarios.add(scenario);
    }


    public void addScenarioOutline(final ScenarioOutlineElement scenarioOutline) {
        scenarioOutline.setParent(this);
        this.scenarioOutlines.add(scenarioOutline);
    }


    @Override
    public Collection<AbstractModelElement> getChildren() {
        final List<AbstractModelElement> results = new ArrayList<AbstractModelElement>();
        if (background != null) {
            results.add(background);
        }
        results.addAll(scenarios);
        results.addAll(scenarioOutlines);

        Collections.sort(results);
        return Collections.unmodifiableCollection(results);
    }


    @Override
    public String toString() {
        return "Feature:" + getText() + "(" + getPosition() + ")";
    }
}
