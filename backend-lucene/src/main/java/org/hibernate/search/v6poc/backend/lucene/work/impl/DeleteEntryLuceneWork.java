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
import org.apache.lucene.index.Term;
import org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneFields;
import org.hibernate.search.v6poc.backend.lucene.logging.impl.Log;
import org.hibernate.search.v6poc.util.spi.Futures;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * @author Guillaume Smet
 */
public class DeleteEntryLuceneWork extends AbstractLuceneWork<Long> {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final String id;

	public DeleteEntryLuceneWork(String indexName, String id) {
		super( "deleteEntry", indexName );
		this.id = id;
	}

	@Override
	public CompletableFuture<Long> execute(LuceneIndexWorkExecutionContext context) {
		return Futures.create( () -> deleteDocuments( context.getIndexWriter() ) );
	}

	private CompletableFuture<Long> deleteDocuments(IndexWriter indexWriter) {
		try {
			return CompletableFuture.completedFuture( indexWriter.deleteDocuments( new Term( LuceneFields.idFieldName(), id ) ) );
		}
		catch (IOException e) {
			throw log.unableToDeleteEntryFromIndex( indexName, id );
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "type=" ).append( workType )
				.append( ", indexName=" ).append( indexName )
				.append( ", id=" ).append( id )
				.append( "]" );
		return sb.toString();
	}
}
