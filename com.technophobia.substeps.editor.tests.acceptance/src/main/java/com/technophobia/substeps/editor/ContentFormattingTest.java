package com.technophobia.substeps.editor;

import org.junit.runner.RunWith;

import com.technophobia.substeps.editor.steps.BasicEditorSteps;
import com.technophobia.substeps.editor.steps.ContentFormattingSteps;
import com.technophobia.substeps.editor.steps.SWTBotInitialiser;
import com.technophobia.substeps.runner.JunitFeatureRunner.BeforeAndAfterProcessors;
import com.technophobia.substeps.runner.JunitFeatureRunner.FeatureFiles;

@RunWith(SubStepsSWTBotJunitClassRunner.class)
@FeatureFiles(featureFile = "features/feature-editing/content-formatting.feature", stepImplementations = {
		ContentFormattingSteps.class, BasicEditorSteps.class }, strict = false, nonStrictKeywordPrecedence = {
		"Given", "When", "Then", "And" })
@BeforeAndAfterProcessors({ SWTBotInitialiser.class })
public class ContentFormattingTest {
}