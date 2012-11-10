/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.content.partition;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.definition.BackgroundContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleRowContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioOutlineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.SuggestionType;
import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class ContentTypeRuleBasedPartitionScannerTest {

    private static final String NEWLINE = System.getProperty("line.separator");

    protected static final Collection<String> SUGGESTIONS = Arrays.asList("Given something", "When something else",
            "Then a result");

    private Mockery context;

    private Supplier<PartitionContext> partitionContextSupplier;
    private PartitionContext partitionContext;

    private ContentTypeRuleBasedPartitionScanner partitionScanner;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {

        this.context = new Mockery();

        this.partitionContext = context.mock(PartitionContext.class);
        this.partitionContextSupplier = context.mock(Supplier.class);

        this.partitionScanner = new ContentTypeRuleBasedPartitionScanner(partitionContextSupplier,
                new FeatureContentTypeDefinitionFactory());

    }


    @Test
    public void canPartitionComments() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE + "Given something" + NEWLINE
                + "#This is comment line 2";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(4, result.length);

        checkType(result[0], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 1" + NEWLINE);
        checkType(result[3], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 2");
    }


    @Test
    public void canPartitionInlineComments() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE
                + "Given something # with a following comment" + NEWLINE + "#This is comment line 2";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[0], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 1" + NEWLINE);
        checkType(result[2], StepContentTypeDefinition.CONTENT_TYPE_ID, "Given something ");
        checkType(result[3], CommentContentTypeDefinition.CONTENT_TYPE_ID, "# with a following comment" + NEWLINE);
        checkType(result[4], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 2");
    }


    @Test
    public void canPartitionSingleTagOnLine() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE + "Given something" + NEWLINE
                + NEWLINE + "Tags: tag-1" + NEWLINE + "Scenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[3], TagContentTypeDefinition.CONTENT_TYPE_ID, "Tags: tag-1" + NEWLINE);
    }


    @Test
    public void canPartitionBackground() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE + "Given something" + NEWLINE
                + NEWLINE + "Tags: tag-1" + NEWLINE + "Scenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[1], BackgroundContentTypeDefinition.CONTENT_TYPE_ID, "Background:" + NEWLINE);
    }


    @Test
    public void canPartitionScenario() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE + "Given something" + NEWLINE
                + NEWLINE + "Tags: tag-1" + NEWLINE + "Scenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[4], ScenarioContentTypeDefinition.CONTENT_TYPE_ID, "Scenario:A scenario");
    }


    @Test
    public void canPartitionScenarioOutline() {

        preparePartitionContext();

        final String text = "#This is comment line 1" + NEWLINE + "Background:" + NEWLINE + "Given something" + NEWLINE
                + NEWLINE + "Tags: tag-1" + NEWLINE + "Scenario Outline:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[4], ScenarioOutlineContentTypeDefinition.CONTENT_TYPE_ID, "Scenario Outline:A scenario");
    }


    @Test
    public void canPartitionExample() {

        preparePartitionContext();

        final String text = "Tags: tag-1" + NEWLINE + "Scenario Outline:A scenario" + NEWLINE + "Given something"
                + NEWLINE + "When something else" + NEWLINE + "Then a result" + NEWLINE + "Examples:" + NEWLINE
                + "\t|example1|example2|";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(8, result.length);

        checkType(result[5], ScenarioExampleContentTypeDefinition.CONTENT_TYPE_ID, "Examples:" + NEWLINE + "");
    }


    @Test
    public void canPartitionExampleRow() {

        preparePartitionContext();

        final String text = "Tags: tag-1" + NEWLINE + "Scenario Outline:A scenario" + NEWLINE + "Given something"
                + NEWLINE + "When something else" + NEWLINE + "Then a result" + NEWLINE + NEWLINE + "Examples:"
                + NEWLINE + "\t|example1|example2|";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(8, result.length);

        checkType(result[7], ScenarioExampleRowContentTypeDefinition.CONTENT_TYPE_ID, "|example1|example2|");
    }


    @Test
    public void canPartitionSteps() {
        preparePartitionContext();

        final String text = "Tags: tag-1" + NEWLINE + "Scenario:A scenario" + NEWLINE + "Given something" + NEWLINE
                + "When something else" + NEWLINE + "Then a result";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[2], StepContentTypeDefinition.CONTENT_TYPE_ID, "Given something" + NEWLINE);
        checkType(result[3], StepContentTypeDefinition.CONTENT_TYPE_ID, "When something else" + NEWLINE);
        checkType(result[4], StepContentTypeDefinition.CONTENT_TYPE_ID, "Then a result");
    }


    @SuppressWarnings("boxing")
    private void checkType(final ITypedRegion typedRegion, final String type, final String text) {
        assertThat(typedRegion.getType(), is(type));
        assertThat(typedRegion.getLength(), is(text.length()));
    }


    private IDocumentPartitioner createPartitionerForDocumentWithText(final String text) {
        final IDocumentPartitioner partitioner = createPartitioner();
        final Document document = new Document(text);
        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);
        return partitioner;
    }


    private void preparePartitionContext() {

        final IProject project = context.mock(IProject.class);
        final ContextualSuggestionManager suggestionManager = context.mock(ContextualSuggestionManager.class);

        context.checking(new Expectations() {
            {
                allowing(partitionContextSupplier).get();
                will(returnValue(partitionContext));

                allowing(partitionContext).currentProject();
                will(returnValue(project));

                allowing(partitionContext).suggestionManager();
                will(returnValue(suggestionManager));

                allowing(suggestionManager).suggestionsFor(SuggestionType.FEATURE, project);
                will(returnValue(SUGGESTIONS));
            }
        });
    }


    private IDocumentPartitioner createPartitioner() {
        final Collection<String> ids = new ArrayList<String>(13);
        ids.add(FeatureContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(BackgroundContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(CommentContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(TagContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(ScenarioContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(ScenarioOutlineContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(ScenarioExampleContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(ScenarioExampleRowContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(StepContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(DefineContentTypeDefinition.CONTENT_TYPE_ID);
        return new FastPartitioner(partitionScanner, ids.toArray(new String[ids.size()]));
    }
}
