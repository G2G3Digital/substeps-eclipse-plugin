package com.technophobia.substeps.editor;

import org.junit.runner.RunWith;

import com.technophobia.substeps.editor.steps.BasicEditorSteps;
import com.technophobia.substeps.editor.steps.ContentFormattingSteps;
import com.technophobia.substeps.editor.steps.SWTBotInitialiser;
import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;

@RunWith(SubStepsSWTBotJunitClassRunner.class)
@SubStepsConfiguration(featureFile = "features/feature-editing/content-formatting.feature", stepImplementations = {
        ContentFormattingSteps.class, BasicEditorSteps.class }, strict = false, nonStrictKeywordPrecedence = { "Given",
        "When", "Then", "And" }, beforeAndAfterImplementations = { SWTBotInitialiser.class })
public class ContentFormattingTest {
}