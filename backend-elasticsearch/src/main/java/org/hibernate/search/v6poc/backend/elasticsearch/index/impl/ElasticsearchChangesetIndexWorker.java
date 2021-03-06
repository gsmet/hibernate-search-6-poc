/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.index.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchDocumentBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.ElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWork;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWorkFactory;
import org.hibernate.search.v6poc.backend.index.spi.ChangesetIndexWorker;
import org.hibernate.search.v6poc.engine.spi.SessionContext;


/**
 * @author Yoann Rodiere
 */
public class ElasticsearchChangesetIndexWorker extends ElasticsearchIndexWorker
		implements ChangesetIndexWorker<ElasticsearchDocumentBuilder> {

	private final ElasticsearchWorkOrchestrator orchestrator;
	private final List<ElasticsearchWork<?>> works = new ArrayList<>();

	public ElasticsearchChangesetIndexWorker(ElasticsearchWorkFactory factory,
			ElasticsearchWorkOrchestrator orchestrator,
			String indexName, SessionContext context) {
		super( factory, indexName, context );
		this.orchestrator = orchestrator;
	}

	@Override
	protected void collect(ElasticsearchWork<?> work) {
		works.add( work );
	}

	@Override
	public CompletableFuture<?> execute() {
		try {
			CompletableFuture<?> future = orchestrator.submit( works );
			return future;
		}
		finally {
			works.clear();
		}
	}

}
