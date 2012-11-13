package com.technophobia.substeps.editor.outline.substeps;

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
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.model.SubstepsDefinitionElement;
import com.technophobia.substeps.editor.outline.model.SubstepsRootElement;
import com.technophobia.substeps.supplier.Transformer;

public class FileToSubstepDefinitionElementTransformerIntegrationTest {
    private static final String FILENAME = "resources/substepsfiletoelementtest.substeps";
    private File file;

    private Transformer<File, AbstractModelElement> transformer;


    @Before
    public void initialise() {

        this.file = new File(FILENAME);

        this.transformer = new FileToSubstepDefinitionElementTransformer(new LineNumberAsPositionTransformer());
    }


    @Test
    public void canParseSubstepsFile() {

        final AbstractModelElement root = transformer.from(file);

        checkSubstepRootIsValid(root);

        final SubstepsRootElement substepRoot = (SubstepsRootElement) root;
        checkDefinitionsAreValid(substepRoot.getSubstepsDefinitions());
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

        assertThat(Integer.valueOf(definition.getSteps().size()), is(Integer.valueOf(expectedSteps.length)));
        checkSteps(definition.getSteps(), expectedSteps);
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

    private static final class LineNumberAsPositionTransformer implements Transformer<Integer, Position> {
        @Override
        public Position from(final Integer from) {
            return new Position(from.intValue());
        }
    }
}
