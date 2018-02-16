/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.work.impl;

import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.hibernate.search.v6poc.search.SearchResult;


/**
 * @author Guillaume Smet
 */
public class StubLuceneWorkFactory implements LuceneWorkFactory {

	@Override
	public LuceneWork<?> createIndex(String indexName) {
		return new StubLuceneWork<>( "createIndex" )
				.addParam( "indexName", indexName );
	}

	@Override
	public LuceneWork<?> add(String indexName, String id, String routingKey, Document document) {
		StubLuceneWork<?> work = new StubLuceneWork<>( "add", document )
				.addParam( "indexName", indexName )
				.addParam( "id", id );
		if ( routingKey != null ) {
			work.addParam( "_routing", routingKey );
		}
		return work;
	}

	@Override
	public LuceneWork<?> update(String indexName, String id, String routingKey, Document document) {
		StubLuceneWork<?> work = new StubLuceneWork<>( "update", document )
				.addParam( "indexName", indexName )
				.addParam( "id", id );
		if ( routingKey != null ) {
			work.addParam( "_routing", routingKey );
		}
		return work;
	}

	@Override
	public LuceneWork<?> delete(String indexName, String id, String routingKey) {
		StubLuceneWork<?> work = new StubLuceneWork<>( "delete" )
				.addParam( "indexName", indexName )
				.addParam( "id", id );
		if ( routingKey != null ) {
			work.addParam( "_routing", routingKey );
		}
		return work;
	}

	@Override
	public LuceneWork<?> flush(String indexName) {
		return new StubLuceneWork<>( "flush" )
				.addParam( "indexName", indexName );
	}

	@Override
	public LuceneWork<?> optimize(String indexName) {
		return new StubLuceneWork<>( "optimize" )
				.addParam( "indexName", indexName );
	}

	@Override
	public <T> LuceneWork<SearchResult<T>> search(Set<String> indexNames, Query query, Long offset, Long limit) {
		return new StubLuceneWork<SearchResult<T>>( "query", query )
				.addParam( "indexName", indexNames )
				.addParam( "offset", offset, String::valueOf )
				.addParam( "limit", limit, String::valueOf );
	}

}
