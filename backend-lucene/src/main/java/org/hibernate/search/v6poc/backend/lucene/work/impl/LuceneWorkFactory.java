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
public interface LuceneWorkFactory {

	// XXX GSM is the createIndex operation really useful?
	LuceneWork<?> createIndex(String indexName);

	LuceneWork<?> add(String indexName, String id, String routingKey, Document document);

	LuceneWork<?> update(String indexName, String id, String routingKey, Document document);

	LuceneWork<?> delete(String indexName, String id, String routingKey);

	LuceneWork<?> flush(String indexName);

	LuceneWork<?> optimize(String indexName);

	// XXX GSM: see if the SearchResultExtractor makes sense
	<T> LuceneWork<SearchResult<T>> search(Set<String> indexNames, Query query, Long offset, Long limit);
}
