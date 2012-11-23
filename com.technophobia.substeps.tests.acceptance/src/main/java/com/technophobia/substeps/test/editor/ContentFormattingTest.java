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
package com.technophobia.substeps.test.editor;

import org.junit.runner.RunWith;

import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;
import com.technophobia.substeps.test.editor.steps.ContentFormattingSteps;
import com.technophobia.substeps.test.runner.SubStepsSWTBotJunitClassRunner;
import com.technophobia.substeps.test.steps.BasicWorkspaceSteps;
import com.technophobia.substeps.test.steps.SWTBotInitialiser;

@RunWith(SubStepsSWTBotJunitClassRunner.class)
@SubStepsConfiguration(featureFile = "features/feature-editing/content-formatting.feature", stepImplementations = {
        ContentFormattingSteps.class, BasicWorkspaceSteps.class }, strict = false, nonStrictKeywordPrecedence = {
        "Given", "When", "Then", "And" }, beforeAndAfterImplementations = { SWTBotInitialiser.class })
public class ContentFormattingTest {
    // No-op
}
