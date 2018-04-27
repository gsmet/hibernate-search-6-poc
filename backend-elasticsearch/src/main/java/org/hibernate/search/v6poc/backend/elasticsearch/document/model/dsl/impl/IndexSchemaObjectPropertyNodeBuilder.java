/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.document.model.dsl.impl;

import org.hibernate.search.v6poc.backend.document.model.dsl.ObjectFieldStorage;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchIndexObjectFieldAccessor;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaNodeCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaNodeContributor;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaObjectNode;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.DataType;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.DynamicType;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.PropertyMapping;
import org.hibernate.search.v6poc.backend.elasticsearch.gson.impl.JsonAccessor;
import org.hibernate.search.v6poc.backend.elasticsearch.gson.impl.JsonObjectAccessor;

class IndexSchemaObjectPropertyNodeBuilder extends AbstractIndexSchemaObjectNodeBuilder
		implements ElasticsearchIndexSchemaNodeContributor<PropertyMapping> {

	private final String absoluteFieldPath;
	private final String relativeFieldName;

	private ObjectFieldStorage storage = ObjectFieldStorage.DEFAULT;

	IndexSchemaObjectPropertyNodeBuilder(String parentPath, String relativeFieldName) {
		this.absoluteFieldPath = parentPath == null ? relativeFieldName : parentPath + "." + relativeFieldName;
		this.relativeFieldName = relativeFieldName;
	}

	@Override
	public String getAbsolutePath() {
		return absoluteFieldPath;
	}

	public void setStorage(ObjectFieldStorage storage) {
		this.storage = storage;
	}

	@Override
	public PropertyMapping contribute(
			ElasticsearchIndexSchemaNodeCollector collector,
			ElasticsearchIndexSchemaObjectNode parentNode) {
		ElasticsearchIndexSchemaObjectNode node = new ElasticsearchIndexSchemaObjectNode( parentNode, absoluteFieldPath, storage );
		collector.collect( absoluteFieldPath, node );

		JsonObjectAccessor jsonAccessor = JsonAccessor.root().property( relativeFieldName ).asObject();

		accessor.initialize( new ElasticsearchIndexObjectFieldAccessor( jsonAccessor, node ) );

		PropertyMapping mapping = new PropertyMapping();
		DataType dataType = DataType.OBJECT;
		switch ( storage ) {
			case DEFAULT:
				break;
			case FLATTENED:
				dataType = DataType.OBJECT;
				break;
			case NESTED:
				dataType = DataType.NESTED;
				break;
		}
		mapping.setType( dataType );

		// TODO allow to configure this, both at index level (configuration properties) and at field level (ElasticsearchExtension)
		mapping.setDynamic( DynamicType.STRICT );

		contributeChildren( mapping, node, collector );

		return mapping;
	}
}