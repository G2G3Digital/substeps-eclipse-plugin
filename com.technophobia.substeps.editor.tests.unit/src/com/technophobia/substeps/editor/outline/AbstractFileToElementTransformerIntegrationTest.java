package com.technophobia.substeps.editor.outline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.text.Position;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.supplier.Transformer;

public abstract class AbstractFileToElementTransformerIntegrationTest<Element extends AbstractModelElement> {

    private File file;

    private Transformer<File, AbstractModelElement> transformer;


    @Before
    public void initialise() {
        this.file = new File(filename());

        this.transformer = createTransformer(new LineNumberAsPositionTransformer());
    }


    @SuppressWarnings("unchecked")
    protected Element doTransformation() {
        final AbstractModelElement result = transformer.from(file);
        return (Element) result;
    }


    protected abstract String filename();


    protected abstract Transformer<File, AbstractModelElement> createTransformer(
            Transformer<Integer, Position> lineNumberToPositionTransformer);


    protected void checkText(final AbstractModelElement element, final String expectedText) {
        assertThat(element.getText(), is(expectedText));
    }


    protected void checkSteps(final Collection<StepElement> steps, final String... expectedSteps) {
        assertThat(Integer.valueOf(steps.size()), is(Integer.valueOf(expectedSteps.length)));

        final Iterator<StepElement> it = steps.iterator();
        for (final String expectedStep : expectedSteps) {
            assertTrue(it.hasNext());
            assertThat(it.next().getText(), is(expectedStep));
        }
    }


    protected void checkExampleRows(final Collection<ExampleRowElement> exampleRows, final String... expectedRows) {
        assertThat(Integer.valueOf(exampleRows.size()), is(Integer.valueOf(expectedRows.length)));

        final Iterator<ExampleRowElement> it = exampleRows.iterator();
        for (final String expectedRow : expectedRows) {
            assertTrue(it.hasNext());
            assertThat(it.next().getText(), is(expectedRow));
        }
    }


    protected <T extends AbstractModelElement> T getSingleElementOfType(final Class<T> modelClass,
            final Collection<AbstractModelElement> items) {
        final Collection<T> elements = getElementsOfType(modelClass, items);
        if (elements.isEmpty()) {
            return null;
        } else if (elements.size() == 1) {
            return elements.iterator().next();
        }
        throw new IllegalStateException("Tried to get a single element of type " + modelClass.getName()
                + ", but instead found " + elements.size());
    }


    @SuppressWarnings("unchecked")
    protected <T extends AbstractModelElement> Collection<T> getElementsOfType(final Class<T> modelClass,
            final Collection<AbstractModelElement> items) {
        return (Collection<T>) Collections2.filter(items, new Predicate<AbstractModelElement>() {
            @Override
            public boolean apply(final AbstractModelElement item) {
                return item.getClass().equals(modelClass);
            }
        });
    }

    private static final class LineNumberAsPositionTransformer implements Transformer<Integer, Position> {
        @Override
        public Position from(final Integer from) {
            return new Position(from.intValue());
        }
    }
}
