/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.index.impl;

import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchDocumentBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.ElasticsearchIndexModel;
import org.hibernate.search.v6poc.backend.elasticsearch.impl.ElasticsearchBackend;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.ElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.search.impl.ElasticsearchSearchTarget;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWorkFactory;
import org.hibernate.search.v6poc.backend.index.spi.ChangesetIndexWorker;
import org.hibernate.search.v6poc.backend.index.spi.IndexManager;
import org.hibernate.search.v6poc.backend.index.spi.SearchTarget;
import org.hibernate.search.v6poc.backend.index.spi.StreamIndexWorker;
import org.hibernate.search.v6poc.engine.spi.SessionContext;


/**
 * @author Yoann Rodiere
 */
public class ElasticsearchIndexManager implements IndexManager<ElasticsearchDocumentBuilder> {

	private final ElasticsearchBackend backend;
	private final String name;
	private final ElasticsearchIndexModel model;
	private final ElasticsearchWorkFactory workFactory;
	private final ElasticsearchWorkOrchestrator changesetOrchestrator;
	private final ElasticsearchWorkOrchestrator streamOrchestrator;

	public ElasticsearchIndexManager(ElasticsearchBackend backend, String name, ElasticsearchIndexModel model) {
		this.backend = backend;
		this.name = name;
		this.model = model;
		this.workFactory = backend.getWorkFactory();
		this.changesetOrchestrator = backend.createChangesetOrchestrator();
		this.streamOrchestrator = backend.getStreamOrchestrator();
	}

	public String getName() {
		return name;
	}

	public ElasticsearchIndexModel getModel() {
		return model;
	}

	@Override
	public ChangesetIndexWorker<ElasticsearchDocumentBuilder> createWorker(SessionContext context) {
		return new ElasticsearchChangesetIndexWorker( workFactory, changesetOrchestrator, name, context );
	}

	@Override
	public StreamIndexWorker<ElasticsearchDocumentBuilder> createStreamWorker(SessionContext context) {
		return new ElasticsearchStreamIndexWorker( workFactory, streamOrchestrator, name, context );
	}

	@Override
	public SearchTarget createSearchTarget() {
		return new ElasticsearchSearchTarget( backend, this );
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
