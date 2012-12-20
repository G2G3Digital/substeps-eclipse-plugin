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
package com.technophobia.substeps.editor.outline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.Position;
import org.jmock.Mockery;
import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.technophobia.substeps.editor.outline.model.AbstractModelElement;
import com.technophobia.substeps.editor.outline.model.ExampleRowElement;
import com.technophobia.substeps.editor.outline.model.StepElement;
import com.technophobia.substeps.editor.outline.substeps.ProjectFile;
import com.technophobia.substeps.supplier.Callback2;
import com.technophobia.substeps.supplier.Transformer;

public abstract class AbstractFileToElementTransformerIntegrationTest<Element extends AbstractModelElement> {

    private Mockery context;
    private IProject project;
    private File file;

    private Transformer<ProjectFile, AbstractModelElement> transformer;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.file = new File(filename());
        this.project = context.mock(IProject.class);

        this.transformer = createTransformer(new LineNumberAsPositionTransformer());
    }


    @SuppressWarnings("unchecked")
    protected Element doTransformation() {
        final AbstractModelElement result = transformer.from(new ProjectFile(project, file));
        return (Element) result;
    }


    protected abstract String filename();


    protected abstract Transformer<ProjectFile, AbstractModelElement> createTransformer(
            Transformer<Integer, Position> lineNumberToPositionTransformer);


    protected void prepareProject(final Callback2<IProject, Mockery> callback) {
        callback.doCallback(project, context);
    }


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
