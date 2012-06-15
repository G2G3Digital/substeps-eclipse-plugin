package com.technophobia.substeps.editor;

import org.junit.runner.RunWith;

import com.technophobia.substeps.editor.steps.BasicEditorSteps;
import com.technophobia.substeps.editor.steps.ContentFormattingSteps;
import com.technophobia.substeps.editor.steps.SWTBotInitialiser;
import com.technophobia.substeps.editor.steps.SimpleSteps;
import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;

@RunWith(SubStepsSWTBotJunitClassRunner.class)
@SubStepsConfiguration(featureFile = "features/sample.feature", stepImplementations = { ContentFormattingSteps.class,
        SimpleSteps.class, BasicEditorSteps.class }, beforeAndAfterImplementations = { SWTBotInitialiser.class })
public class SimpleTest {
}
