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
package com.technophobia.substeps.editor.outline.substeps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.text.Position;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.editor.outline.AbstractFileToElementTransformerIntegrationTest;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.model.SubstepsDefinitionElement;
import com.technophobia.substeps.editor.outline.model.SubstepsRootElement;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class FileToSubstepDefinitionElementTransformerIntegrationTest extends
        AbstractFileToElementTransformerIntegrationTest<SubstepsRootElement> {

    private static final String FILENAME = "resources/substepsfiletoelementtest.substeps";


    @Test
    public void canParseSubstepsFile() {

        final SubstepsRootElement root = doTransformation();

        checkSubstepRootIsValid(root);

        checkDefinitionsAreValid(getElementsOfType(SubstepsDefinitionElement.class, root.getChildren()));
    }


    @Override
    protected String filename() {
        return FILENAME;
    }


    @Override
    protected Transformer<ProjectFile, AbstractModelElement> createTransformer(
            final Transformer<Integer, Position> lineNumberToPositionTransformer) {
        return new FileToSubstepDefinitionElementTransformer(lineNumberToPositionTransformer);
    }


    private void checkSubstepRootIsValid(final AbstractModelElement root) {
        assertThat(root, is(SubstepsRootElement.class));
        checkText(root, "Substep");
    }


    private void checkDefinitionsAreValid(final Collection<SubstepsDefinitionElement> definitions) {
        assertThat(Integer.valueOf(definitions.size()), is(Integer.valueOf(4)));

        final Iterator<SubstepsDefinitionElement> it = definitions.iterator();
        checkDefinition(it.next(), "1st substep", "Given a 1st substep", "When a 1st substep", "Then a 1st substep");
        checkDefinition(it.next(), "2nd substep", "Given a 2nd substep", "When a 2nd substep", "Then a 2nd substep");
        checkDefinition(it.next(), "3rd substep", "Given a 3rd substep", "When a 3rd substep", "Then a 3rd substep");
        checkDefinition(it.next(), "4th substep", "Given a 4th substep", "When a 4th substep", "Then a 4th substep");
    }


    private void checkDefinition(final SubstepsDefinitionElement definition, final String name,
            final String... expectedSteps) {
        checkText(definition, name);

        final Collection<StepElement> steps = getElementsOfType(StepElement.class, definition.getChildren());

        assertThat(Integer.valueOf(steps.size()), is(Integer.valueOf(expectedSteps.length)));
        checkSteps(steps, expectedSteps);
    }
}
