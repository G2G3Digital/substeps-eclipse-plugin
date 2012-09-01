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