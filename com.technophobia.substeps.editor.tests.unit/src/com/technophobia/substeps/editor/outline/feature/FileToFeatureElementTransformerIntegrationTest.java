package com.technophobia.substeps.editor.outline.feature;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import org.eclipse.jface.text.Position;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.editor.outline.AbstractFileToElementTransformerIntegrationTest;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.BackgroundElement;
import com.technophobia.substeps.editor.outline.model.ExampleElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.editor.outline.model.ScenarioElement;
import com.technophobia.substeps.editor.outline.model.ScenarioOutlineElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.substeps.ProjectFile;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class FileToFeatureElementTransformerIntegrationTest extends
        AbstractFileToElementTransformerIntegrationTest<FeatureElement> {

    private static final String FILENAME = "resources/featurefiletoelementtest.feature";


    @Test
    public void canParseFeatureFile() {

        final FeatureElement root = doTransformation();

        checkFeatureIsValid(root);

        final Collection<AbstractModelElement> children = root.getChildren();
        checkBackgroundIsValid(getSingleElementOfType(BackgroundElement.class, children));
        checkScenariosAreValid(getElementsOfType(ScenarioElement.class, children));
        checkScenarioOutlinesAreValid(getElementsOfType(ScenarioOutlineElement.class, children));
    }


    @Override
    protected String filename() {
        return FILENAME;
    }


    @Override
    protected Transformer<ProjectFile, AbstractModelElement> createTransformer(
            final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        return new FileToFeatureElementTransformer(lineNumberToPositionTransformer);
    }


    private void checkFeatureIsValid(final AbstractModelElement root) {
        assertThat(root, is(FeatureElement.class));
        checkText(root, "Test Feature");
    }


    private void checkBackgroundIsValid(final BackgroundElement background) {
        checkText(background, "Some background");
        final Collection<StepElement> steps = getElementsOfType(StepElement.class, background.getChildren());
        checkSteps(steps, "Given a background step", "When the background", "Then a background result");
    }


    private void checkScenariosAreValid(final Collection<ScenarioElement> scenarios) {
        assertThat(Integer.valueOf(scenarios.size()), is(Integer.valueOf(1)));

        final ScenarioElement scenario = scenarios.iterator().next();
        checkText(scenario, "1st scenario");
        final Collection<StepElement> steps = getElementsOfType(StepElement.class, scenario.getChildren());
        checkSteps(steps, "Given a 1st scenario", "When a 1st scenario", "Then a 1st scenario");
    }


    private void checkScenarioOutlinesAreValid(final Collection<ScenarioOutlineElement> scenarioOutlines) {
        assertThat(Integer.valueOf(scenarioOutlines.size()), is(Integer.valueOf(1)));

        final ScenarioOutlineElement scenarioOutline = scenarioOutlines.iterator().next();
        final Collection<StepElement> steps = getElementsOfType(StepElement.class, scenarioOutline.getChildren());
        final ExampleElement example = getSingleElementOfType(ExampleElement.class, scenarioOutline.getChildren());
        final Collection<ExampleRowElement> exampleRows = getElementsOfType(ExampleRowElement.class,
                example.getChildren());

        checkText(scenarioOutline, "1st scenario outline");
        checkSteps(steps, "Given a 1st scenario outline", "When a 1st scenario outline", "Then a 1st scenario outline");

        checkExampleRows(exampleRows, "|cell 11|cell 12|cell 13|", "|cell 21|cell 22|cell 23|",
                "|cell 31|cell 32|cell 33|");

    }

}
