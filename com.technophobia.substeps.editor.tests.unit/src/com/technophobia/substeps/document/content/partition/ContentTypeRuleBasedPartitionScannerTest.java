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

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;

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

		checkType(result[0], FeatureContentTypeDefinition.COMMENT.id(), "#This is comment line 1\n");
		checkType(result[3], FeatureContentTypeDefinition.COMMENT.id(), "#This is comment line 2");
	}

	@Test
	public void canPartitionSingleTagOnLine() {
		final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(6, result.length);

		checkType(result[4], FeatureContentTypeDefinition.TAG.id(), "Tags: tag-1\n");
	}

	@Test
	public void canPartitionBackground() {
		final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(6, result.length);

		checkType(result[1], FeatureContentTypeDefinition.BACKGROUND.id(), "Background:\n");
	}

	@Test
	public void canPartitionScenario() {

		final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario:A scenario";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(6, result.length);

		checkType(result[5], FeatureContentTypeDefinition.SCENARIO.id(), "Scenario:A scenario");
	}

	@Test
	public void canPartitionScenarioOutline() {
		final String text = "#This is comment line 1\nBackground:\nGiven something\n\nTags: tag-1\nScenario Outline:A scenario";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(6, result.length);

		checkType(result[5], FeatureContentTypeDefinition.SCENARIO_OUTLINE.id(), "Scenario Outline:A scenario");
	}

	@Test
	public void canPartitionExample() {
		final String text = "@tag-1\nScenario Outline:A scenario\nGiven Something\nWhen something else\nThen a result\n\nExamples:\n\t|example1|example2|";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(9, result.length);

		checkType(result[6], FeatureContentTypeDefinition.EXAMPLE.id(), "Examples:\n");
	}

	@Test
	public void canPartitionExampleRow() {
		final String text = "@tag-1\nScenario Outline:A scenario\nGiven Something\nWhen something else\nThen a result\n\nExamples:\n\t|example1|example2|";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(9, result.length);

		checkType(result[8], FeatureContentTypeDefinition.EXAMPLE_ROW.id(), "|example1|example2|");
	}

	@Test
	public void canPartitionGiven() {
		final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(5, result.length);

		checkType(result[2], FeatureContentTypeDefinition.GIVEN.id(), "Given Something\n");
	}

	@Test
	public void canPartitionWhen() {
		final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(5, result.length);

		checkType(result[3], FeatureContentTypeDefinition.WHEN.id(), "When something else\n");
	}

	@Test
	public void canPartitionThen() {
		final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nThen a result";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(5, result.length);

		checkType(result[4], FeatureContentTypeDefinition.THEN.id(), "Then a result");
	}

	@Test
	public void canPartitionAnd() {
		final String text = "@tag-1\nScenario:A scenario\nGiven Something\nWhen something else\nAnd another thing\nThen a result";
		final IDocumentPartitioner partitioner = createPartitionerForDocumentWithText(text);

		final ITypedRegion[] result = partitioner.computePartitioning(0, text.length());
		assertEquals(6, result.length);

		checkType(result[4], FeatureContentTypeDefinition.AND.id(), "And another thing\n");
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
		final String[] ids = new String[FeatureContentTypeDefinition.values().length];
		for (int i = 0; i < FeatureContentTypeDefinition.values().length; i++) {
			ids[i] = FeatureContentTypeDefinition.values()[i].id();
		}
		return new FastPartitioner(partitionScanner, ids);
	}
}
