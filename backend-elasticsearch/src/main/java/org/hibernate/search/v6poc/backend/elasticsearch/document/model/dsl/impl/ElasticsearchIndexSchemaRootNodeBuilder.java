/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.document.model.dsl.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.search.v6poc.backend.document.model.dsl.spi.IndexSchemaRootNodeBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaNodeCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaObjectNode;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchRootIndexSchemaContributor;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.DynamicType;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.RootTypeMapping;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.RoutingType;
import org.hibernate.search.v6poc.backend.elasticsearch.multitenancy.impl.MultiTenancyStrategy;
import org.hibernate.search.v6poc.logging.spi.FailureContextElement;
import org.hibernate.search.v6poc.logging.spi.FailureContexts;

public class ElasticsearchIndexSchemaRootNodeBuilder extends AbstractElasticsearchIndexSchemaObjectNodeBuilder
		implements IndexSchemaRootNodeBuilder, ElasticsearchRootIndexSchemaContributor {

	private final String hibernateSearchIndexName;
	private final MultiTenancyStrategy multiTenancyStrategy;

	private RoutingType routing = null;

	public ElasticsearchIndexSchemaRootNodeBuilder(String hibernateSearchIndexName,
			MultiTenancyStrategy multiTenancyStrategy) {
		this.hibernateSearchIndexName = hibernateSearchIndexName;
		this.multiTenancyStrategy = multiTenancyStrategy;
	}

	@Override
	public List<FailureContextElement> getFailureContext() {
		return Arrays.asList(
				getIndexFailureContextElement(),
				FailureContexts.indexSchemaRoot()
		);
	}

	@Override
	public void explicitRouting() {
		this.routing = RoutingType.REQUIRED;
	}

	@Override
	public RootTypeMapping contribute(ElasticsearchIndexSchemaNodeCollector collector) {
		ElasticsearchIndexSchemaObjectNode node = ElasticsearchIndexSchemaObjectNode.root();

		RootTypeMapping mapping = new RootTypeMapping();
		if ( routing != null ) {
			mapping.setRouting( routing );
		}

		multiTenancyStrategy.contributeToMapping( mapping );

		// TODO allow to configure this, both at index level (configuration properties) and at field level (ElasticsearchExtension)
		mapping.setDynamic( DynamicType.STRICT );

		contributeChildren( mapping, node, collector );

		return mapping;
	}

	@Override
	ElasticsearchIndexSchemaRootNodeBuilder getRootNodeBuilder() {
		return this;
	}

	@Override
	String getAbsolutePath() {
		return null;
	}

	FailureContextElement getIndexFailureContextElement() {
		return FailureContexts.fromIndexName( hibernateSearchIndexName );
	}
}
