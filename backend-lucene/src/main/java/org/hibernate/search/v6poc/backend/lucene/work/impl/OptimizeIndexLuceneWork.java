/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.work.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

import org.apache.lucene.index.IndexWriter;
import org.hibernate.search.v6poc.backend.lucene.logging.impl.Log;
import org.hibernate.search.v6poc.util.spi.Futures;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * @author Guillaume Smet
 */
public class OptimizeIndexLuceneWork extends AbstractLuceneWork<Long> {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	public OptimizeIndexLuceneWork(String indexName) {
		super( "optimizeIndex", indexName );
	}

	@Override
	public CompletableFuture<Long> execute(LuceneIndexWorkExecutionContext context) {
		return Futures.create( () -> commitIndex( context.getIndexWriter() ) );
	}

	private CompletableFuture<Long> commitIndex(IndexWriter indexWriter) {
		try {
			indexWriter.forceMerge( 1 );
			return CompletableFuture.completedFuture( indexWriter.commit() );
		}
		catch (IOException e) {
			throw log.unableToCommitIndex( indexName );
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "type=" ).append( workType )
				.append( ", indexName=" ).append( indexName )
				.append( "]" );
		return sb.toString();
	}
}