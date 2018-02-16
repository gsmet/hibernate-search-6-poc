/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillaume Smet
 */
public class LuceneIndexModel {

	private final String indexName;
	private final Map<String, LuceneIndexSchemaFieldNode<?>> fieldModels = new HashMap<>();

	public LuceneIndexModel(String indexName, LuceneIndexSchemaCollectorImpl collector) {
		this.indexName = indexName;
		//collector.contribute( fieldModels::put );
	}

	public String getIndexName() {
		return indexName;
	}

	public LuceneIndexSchemaFieldNode<?> getFieldModel(String absolutePath) {
		return fieldModels.get( absolutePath );
	}

	@Override
	public String toString() {
		return new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "indexName=" ).append( indexName )
				.append( "]" )
				.toString();
	}
}
