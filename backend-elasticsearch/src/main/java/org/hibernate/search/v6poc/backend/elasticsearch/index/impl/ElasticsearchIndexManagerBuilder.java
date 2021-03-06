/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.index.impl;

import java.util.Properties;

import org.hibernate.search.v6poc.backend.document.model.spi.IndexModelCollectorImplementor;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchDocumentBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexModel;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexModelCollectorImpl;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.TypeMapping;
import org.hibernate.search.v6poc.backend.elasticsearch.impl.ElasticsearchBackend;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWork;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.engine.spi.BuildContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author Yoann Rodiere
 */
public class ElasticsearchIndexManagerBuilder implements IndexManagerBuilder<ElasticsearchDocumentBuilder> {

	private final ElasticsearchBackend backend;
	private final String indexName;
	private final BuildContext context;
	private final Properties indexProperties;

	private final ElasticsearchIndexModelCollectorImpl<TypeMapping> collector =
			ElasticsearchIndexModelCollectorImpl.root();

	public ElasticsearchIndexManagerBuilder(ElasticsearchBackend backend, String indexName,
			BuildContext context, Properties indexProperties) {
		this.backend = backend;
		this.indexName = indexName;
		this.context = context;
		this.indexProperties = indexProperties;
	}

	@Override
	public IndexModelCollectorImplementor getModelCollector() {
		return collector;
	}

	@Override
	public ElasticsearchIndexManager build() {
		ElasticsearchIndexModel model = new ElasticsearchIndexModel( indexName, collector );

		Gson gson = new Gson();
		JsonObject mappingAsJson = gson.toJsonTree( model.getMapping() ).getAsJsonObject();
		JsonObject modelAsJson = new JsonObject();
		modelAsJson.add( "mapping", mappingAsJson );

		// TODO make sure index initialization is performed in parallel for all indexes?
		ElasticsearchWork<?> work = backend.getWorkFactory().createIndex( indexName, modelAsJson );
		backend.getStreamOrchestrator().submit( work );

		return new ElasticsearchIndexManager( backend, indexName, model );
	}

}
