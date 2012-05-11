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
package com.technophobia.substeps.document.content.feature;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

public class FeatureContentTypeDefinitionFactoryTest {
	
	private ContentTypeDefinitionFactory contentTypeDefinitionFactory;
	
	@Before
	public void initialiseFactory(){
		this.contentTypeDefinitionFactory = new FeatureContentTypeDefinitionFactory();
	}

	@Test
	public void canGetAllFeatureContentTypeDefinitionsByName(){
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_feature"), is(FeatureContentTypeDefinition.FEATURE));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_tag"), is(FeatureContentTypeDefinition.TAG));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_comment"), is(FeatureContentTypeDefinition.COMMENT));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_background"), is(FeatureContentTypeDefinition.BACKGROUND));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_scenario"), is(FeatureContentTypeDefinition.SCENARIO));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_scenario_outline"), is(FeatureContentTypeDefinition.SCENARIO_OUTLINE));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_example"), is(FeatureContentTypeDefinition.EXAMPLE));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_example_row"), is(FeatureContentTypeDefinition.EXAMPLE_ROW));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_given"), is(FeatureContentTypeDefinition.GIVEN));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_when"), is(FeatureContentTypeDefinition.WHEN));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_then"), is(FeatureContentTypeDefinition.THEN));
		assertThat((FeatureContentTypeDefinition)contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_and"), is(FeatureContentTypeDefinition.AND));
	}
	
	@Test(expected=IllegalStateException.class)
	public void throwsExceptionWhenInvalidDefinitionRequested(){
		
		contentTypeDefinitionFactory.contentTypeDefintionByName("invalid_type");
	}
}
