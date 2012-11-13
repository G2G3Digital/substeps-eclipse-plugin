package com.technophobia.substeps.editor.outline.feature;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.text.Position;
import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.BackgroundElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.editor.outline.model.ScenarioElement;
import com.technophobia.substeps.editor.outline.model.ScenarioOutlineElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.supplier.Transformer;

public class FileToFeatureElementTransformerIntegrationTest {

    private static final String FILENAME = "resources/featurefiletoelementtest.feature";
    private File file;

    private Transformer<File, AbstractModelElement> transformer;


    @Before
    public void initialise() {

        this.file = new File(FILENAME);

        this.transformer = new FileToFeatureElementTransformer(new LineNumberAsPositionTransformer());
    }


    @Test
    public void canParseFeatureFile() {

        final AbstractModelElement root = transformer.from(file);

        checkFeatureIsValid(root);

        final FeatureElement feature = (FeatureElement) root;
        checkBackgroundIsValid(feature.getBackground());
        checkScenariosAreValid(feature.getScenarios());
        checkScenarioOutlinesAreValid(feature.getScenarioOutlines());
    }


    private void checkFeatureIsValid(final AbstractModelElement root) {
        assertThat(root, is(FeatureElement.class));
        checkText(root, "Test Feature");
    }


    private void checkBackgroundIsValid(final BackgroundElement background) {
        checkText(background, "Some background");
        checkSteps(background.getSteps(), "Given a background step", "When the background", "Then a background result");
    }


    private void checkScenariosAreValid(final Collection<ScenarioElement> scenarios) {
        assertThat(Integer.valueOf(scenarios.size()), is(Integer.valueOf(1)));

        final ScenarioElement scenario = scenarios.iterator().next();
        checkText(scenario, "1st scenario");
        checkSteps(scenario.getSteps(), "Given a 1st scenario", "When a 1st scenario", "Then a 1st scenario");
    }


    private void checkScenarioOutlinesAreValid(final Collection<ScenarioOutlineElement> scenarioOutlines) {
        assertThat(Integer.valueOf(scenarioOutlines.size()), is(Integer.valueOf(1)));

        final ScenarioOutlineElement scenarioOutline = scenarioOutlines.iterator().next();
        checkText(scenarioOutline, "1st scenario outline");
        checkSteps(scenarioOutline.getSteps(), "Given a 1st scenario outline", "When a 1st scenario outline",
                "Then a 1st scenario outline");

        final Collection<ExampleRowElement> exampleRows = scenarioOutline.getExample().getExampleRows();
        checkExampleRows(exampleRows, "|cell 11|cell 12|cell 13|", "|cell 21|cell 22|cell 23|",
                "|cell 31|cell 32|cell 33|");

    }


    private void checkText(final AbstractModelElement element, final String expectedText) {
        assertThat(element.getText(), is(expectedText));
    }


    private void checkSteps(final Collection<StepElement> steps, final String... expectedSteps) {
        assertThat(Integer.valueOf(steps.size()), is(Integer.valueOf(expectedSteps.length)));

        final Iterator<StepElement> it = steps.iterator();
        for (final String expectedStep : expectedSteps) {
            assertTrue(it.hasNext());
            assertThat(it.next().getText(), is(expectedStep));
        }
    }


    private void checkExampleRows(final Collection<ExampleRowElement> exampleRows, final String... expectedRows) {
        assertThat(Integer.valueOf(exampleRows.size()), is(Integer.valueOf(expectedRows.length)));

        final Iterator<ExampleRowElement> it = exampleRows.iterator();
        for (final String expectedRow : expectedRows) {
            assertTrue(it.hasNext());
            assertThat(it.next().getText(), is(expectedRow));
        }
    }

    private static final class LineNumberAsPositionTransformer implements Transformer<Integer, Position> {
        @Override
        public Position from(final Integer from) {
            return new Position(from.intValue());
        }
    }
}
