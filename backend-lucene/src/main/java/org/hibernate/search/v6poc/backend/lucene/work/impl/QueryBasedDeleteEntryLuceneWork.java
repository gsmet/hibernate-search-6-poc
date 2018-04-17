/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.work.impl;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.hibernate.search.v6poc.backend.lucene.search.impl.LuceneQueries;

/**
 * @author Guillaume Smet
 */
public class QueryBasedDeleteEntryLuceneWork extends AbstractDeleteEntryLuceneWork {

	public QueryBasedDeleteEntryLuceneWork(String indexName, String tenantId, String id) {
		super( indexName, tenantId, id );
	}

	@Override
	protected long doDeleteDocuments(IndexWriter indexWriter, String tenantId, String id) throws IOException {
		return indexWriter.deleteDocuments( LuceneQueries.discriminatorMultiTenancyDeleteDocumentQuery( tenantId, id ) );
	}
}
