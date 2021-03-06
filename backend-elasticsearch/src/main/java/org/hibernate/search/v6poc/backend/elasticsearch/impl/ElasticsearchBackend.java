/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.impl;

import java.util.Properties;

import org.hibernate.search.v6poc.backend.elasticsearch.client.impl.ElasticsearchClient;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchDocumentBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.index.impl.ElasticsearchIndexManagerBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.ElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.StubElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWorkFactory;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.backend.spi.Backend;
import org.hibernate.search.v6poc.engine.spi.BuildContext;


/**
 * @author Yoann Rodiere
 */
public class ElasticsearchBackend implements Backend<ElasticsearchDocumentBuilder> {

	private final ElasticsearchClient client;

	private final String name;

	private final ElasticsearchWorkFactory workFactory;

	private final ElasticsearchWorkOrchestrator streamOrchestrator;

	private final ElasticsearchWorkOrchestrator queryOrchestrator;

	public ElasticsearchBackend(ElasticsearchClient client, String name, ElasticsearchWorkFactory workFactory) {
		this.client = client;
		this.name = name;
		this.workFactory = workFactory;
		this.streamOrchestrator = new StubElasticsearchWorkOrchestrator( client );
		this.queryOrchestrator = new StubElasticsearchWorkOrchestrator( client );
	}

	@Override
	public IndexManagerBuilder<ElasticsearchDocumentBuilder> createIndexManagerBuilder(
			String name, BuildContext context, Properties indexProperties) {
		return new ElasticsearchIndexManagerBuilder( this, name, context, indexProperties );
	}

	public ElasticsearchWorkFactory getWorkFactory() {
		return workFactory;
	}

	public ElasticsearchClient getClient() {
		return client;
	}

	public ElasticsearchWorkOrchestrator createChangesetOrchestrator() {
		return new StubElasticsearchWorkOrchestrator( client );
	}

	public ElasticsearchWorkOrchestrator getStreamOrchestrator() {
		return streamOrchestrator;
	}

	public ElasticsearchWorkOrchestrator getQueryOrchestrator() {
		return queryOrchestrator;
	}

	@Override
	public void close() {
		// TODO use a Closer
		client.close();
		streamOrchestrator.close();
	}

	@Override
	public String toString() {
		return new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "name=" ).append( name )
				.append( "]")
				.toString();
	}

}
