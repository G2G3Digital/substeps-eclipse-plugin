package com.technophobia.substeps.document.content.view.hover.model;

import java.util.List;

import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.Step;

public class SubstepHoverModel extends HoverModel {

    public SubstepHoverModel(final ParentStep parentStep) {
        super(headerFor(parentStep), getStepsAsString(parentStep.getSteps()), getLocationFor(parentStep));
    }


    private static String headerFor(final ParentStep parentStep) {
        return "Define: " + parentStep.getParent().getLine();
    }


    private static String getStepsAsString(final List<Step> steps) {
        final StringBuilder sb = new StringBuilder();
        for (final Step step : steps) {
            sb.append("\t");
            sb.append(step.getLine());
            sb.append("\n");
        }
        return sb.toString();
    }


    private static String getLocationFor(final ParentStep parentStep) {
        return parentStep.getSubStepFile() + ": " + parentStep.getSourceLineNumber();
    }


    public SubstepHoverModel(final String header, final String body, final String location) {
        super(header, body, location);
    }
}
