package com.technophobia.substeps.editor.outline;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Position;

import com.technophobia.substeps.editor.outline.model.AbstractStepContainerElement;
import com.technophobia.substeps.editor.outline.model.BackgroundElement;
import com.technophobia.substeps.editor.outline.model.ExampleElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.FeatureElement;
import com.technophobia.substeps.editor.outline.model.ScenarioElement;
import com.technophobia.substeps.editor.outline.model.ScenarioOutlineElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.model.ExampleParameter;
import com.technophobia.substeps.model.FeatureFile;
import com.technophobia.substeps.model.Scenario;
import com.technophobia.substeps.model.Step;
import com.technophobia.substeps.supplier.Transformer;

public class FeatureFileToElementTransformer implements Transformer<FeatureFile, FeatureElement> {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final static String TAG_POSITIONS = "__tag_positions";
    private final Transformer<Integer, Position> lineNumberToPositionTransformer;


    public FeatureFileToElementTransformer(final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        this.lineNumberToPositionTransformer = lineNumberToPositionTransformer;
    }


    @Override
    public FeatureElement from(final FeatureFile file) {

        final FeatureElement element = new FeatureElement(file.getDescription(), asOffsetPosition(0));

        addBackground(element, file);

        for (final Scenario scenario : file.getScenarios()) {
            addScenarioTo(element, scenario);
        }

        return element;
    }


    private void addScenarioTo(final FeatureElement element, final Scenario scenario) {
        if (scenario.isOutline()) {
            final ScenarioOutlineElement scenarioElement = new ScenarioOutlineElement(scenario.getDescription(),
                    asOffsetPosition(scenario.getScenarioLineNumber()));
            final ExampleElement example = new ExampleElement("Examples",
                    asOffsetPosition(scenario.getExampleKeysLineNumber()));
            scenarioElement.setExample(example);
            for (final ExampleParameter exampleParameter : scenario.getExampleParameters()) {
                example.addExampleRow(new ExampleRowElement(exampleRowFrom(exampleParameter.getParameters()),
                        asOffsetPosition(exampleParameter.getLineNumber())));
            }
            element.addScenarioOutline(scenarioElement);
            addStepsTo(scenarioElement, scenario.getSteps());

        } else {
            final ScenarioElement scenarioElement = new ScenarioElement(scenario.getDescription(),
                    asOffsetPosition(scenario.getScenarioLineNumber()));
            element.addScenario(scenarioElement);
            addStepsTo(scenarioElement, scenario.getSteps());
        }

    }


    private void addBackground(final FeatureElement element, final FeatureFile file) {
        final List<Scenario> scenarios = file.getScenarios();
        if (!scenarios.isEmpty()) {
            final Scenario scenario = scenarios.get(0);
            final String backgroundText = scenario.getBackgroundRawText();
            if (backgroundText != null && backgroundText.length() > 0) {
                final int backgroundLineNumber = backgroundLineNumberFor(scenario);
                final BackgroundElement background = new BackgroundElement(backgroundText.substring(0,
                        backgroundText.indexOf(NEWLINE)), asOffsetPosition(backgroundLineNumber));
                addStepsTo(background, scenario.getBackgroundSteps());
                element.setBackground(background);
            }
        }
    }


    @SuppressWarnings("unused")
    private int backgroundLineNumberFor(final Scenario scenario) {
        // if (scenario.getBackgroundSteps().size() > 0) {
        // return scenario.getSteps().get(0).getSourceLineNumber();
        // }
        return 0;
    }


    private void addStepsTo(final AbstractStepContainerElement stepContainerElement, final List<Step> steps) {
        for (final Step step : steps) {
            stepContainerElement.addStep(new StepElement(step.getLine(), asOffsetPosition(step.getSourceLineNumber())));
        }
    }


    private String exampleRowFrom(final Map<String, String> examples) {
        final StringBuilder sb = new StringBuilder();
        for (final String example : examples.values()) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(example);
        }
        return sb.toString();
    }


    private Position asOffsetPosition(final int lineNumber) {
        return lineNumberToPositionTransformer.from(Integer.valueOf(lineNumber));
    }
}
