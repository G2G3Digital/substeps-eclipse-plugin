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
import java.util.Collection;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.definition.AndContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.BackgroundContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.GivenContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleRowContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioOutlineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ThenContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.WhenContentTypeDefinition;

public class ContentTypeRuleBasedPartitionScannerTest {

    ContentTypeRuleBasedPartitionScanner partitionScanner;


    @Before
    public void initScanner() {
        this.partitionScanner = new ContentTypeRuleBasedPartitionScanner(new FeatureContentTypeDefinitionFactory());
    }


    @Test
    public void canPartitionComments() {
        final String text = "#This is comment line 1\nBackground:\nGiven something\n#This is comment line 2";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(4, result.length);

        checkType(result[0], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 1\n");
        checkType(result[3], CommentContentTypeDefinition.CONTENT_TYPE_ID, "#This is comment line 2");
    }


    @Test
    public void canPartitionSingleTagOnLine() {
        final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(6, result.length);

        checkType(result[4], TagContentTypeDefinition.CONTENT_TYPE_ID, "Tags: tag-1\n");
    }


    @Test
    public void canPartitionBackground() {
        final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(6, result.length);

        checkType(result[1], BackgroundContentTypeDefinition.CONTENT_TYPE_ID, "Background:\n");
    }


    @Test
    public void canPartitionScenario() {

        final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(6, result.length);

        checkType(result[5], ScenarioContentTypeDefinition.CONTENT_TYPE_ID, "Scenario:A scenario");
    }


    @Test
    public void canPartitionScenarioOutline() {
        final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario Outline:A scenario";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(6, result.length);

        checkType(result[5], ScenarioOutlineContentTypeDefinition.CONTENT_TYPE_ID, "Scenario Outline:A scenario");
    }


    @Test
    public void canPartitionExample() {
        final String text = "@tag-1\nScenario Outline:A scenario\nGiven Something\nWhen something else\nThen a result\n\nExamples:\n\t|example1|example2|";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(9, result.length);

        checkType(result[6], ScenarioExampleContentTypeDefinition.CONTENT_TYPE_ID, "Examples:\n");
    }


    @Test
    public void canPartitionExampleRow() {
        final String text = "@tag-1\nScenario Outline:A scenario\nGiven Something\nWhen something else\nThen a result\n\nExamples:\n\t|example1|example2|";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(9, result.length);

        checkType(result[8], ScenarioExampleRowContentTypeDefinition.CONTENT_TYPE_ID, "|example1|example2|");
    }


    @Test
    public void canPartitionGiven() {
        final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[2], GivenContentTypeDefinition.CONTENT_TYPE_ID, "Given Something\n");
    }


    @Test
    public void canPartitionWhen() {
        final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[3], WhenContentTypeDefinition.CONTENT_TYPE_ID, "When something else\n");
    }


    @Test
    public void canPartitionThen() {
        final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(5, result.length);

        checkType(result[4], ThenContentTypeDefinition.CONTENT_TYPE_ID, "Then a result");
    }


    @Test
    public void canPartitionAnd() {
        final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nAnd another thing\nThen a result";
        final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

        final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
        assertEquals(6, result.length);

        checkType(result[4], AndContentTypeDefinition.CONTENT_TYPE_ID, "And another thing\n");
    }


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
        ids.add(GivenContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(WhenContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(ThenContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(AndContentTypeDefinition.CONTENT_TYPE_ID);
        ids.add(DefineContentTypeDefinition.CONTENT_TYPE_ID);
        return new FastPartitioner(partitionScanner, ids.toArray(new String[ids.size()]));
    }
}
