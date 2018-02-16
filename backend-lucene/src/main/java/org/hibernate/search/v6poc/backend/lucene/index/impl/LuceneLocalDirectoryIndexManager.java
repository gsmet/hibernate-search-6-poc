/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.index.impl;

import org.hibernate.search.v6poc.backend.index.spi.ChangesetIndexWorker;
import org.hibernate.search.v6poc.backend.index.spi.IndexSearchTargetBuilder;
import org.hibernate.search.v6poc.backend.index.spi.StreamIndexWorker;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneIndexModel;
import org.hibernate.search.v6poc.backend.lucene.impl.LuceneBackend;
import org.hibernate.search.v6poc.backend.lucene.orchestration.impl.LuceneWorkOrchestrator;
import org.hibernate.search.v6poc.backend.lucene.work.impl.LuceneWorkFactory;
import org.hibernate.search.v6poc.engine.spi.SessionContext;


/**
 * @author Guillaume Smet
 */
public class LuceneLocalDirectoryIndexManager implements LuceneIndexManager {

	private final LuceneBackend backend;
	private final String name;
	private final LuceneIndexModel model;
	private final LuceneWorkFactory workFactory;
	private final LuceneWorkOrchestrator changesetOrchestrator;
	private final LuceneWorkOrchestrator streamOrchestrator;

	public LuceneLocalDirectoryIndexManager(LuceneBackend backend, String name, LuceneIndexModel model) {
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

	public LuceneIndexModel getModel() {
		return model;
	}

	@Override
	public ChangesetIndexWorker<LuceneDocumentBuilder> createWorker(SessionContext context) {
		return new LuceneChangesetIndexWorker( workFactory, changesetOrchestrator, name, context );
	}

	@Override
	public StreamIndexWorker<LuceneDocumentBuilder> createStreamWorker(SessionContext context) {
		return new LuceneStreamIndexWorker( workFactory, streamOrchestrator, name, context );
	}

	@Override
	public IndexSearchTargetBuilder createSearchTarget() {
		// XXX GSM: implement Search
		throw new UnsupportedOperationException( "Search is not implemented yet" );
	}

	@Override
	public void addToSearchTarget(IndexSearchTargetBuilder searchTargetBuilder) {
		// XXX GSM: implement Search
		throw new UnsupportedOperationException( "Search is not implemented yet" );
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
