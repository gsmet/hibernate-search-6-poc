/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.search.query.impl;

import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneFields;
import org.hibernate.search.v6poc.backend.lucene.search.impl.LuceneDocumentReference;
import org.hibernate.search.v6poc.search.DocumentReference;
import org.hibernate.search.v6poc.search.query.spi.ProjectionHitCollector;
import org.hibernate.search.v6poc.util.impl.CollectionHelper;

class DocumentReferenceProjectionDocumentExtractor implements ProjectionDocumentExtractor {

	private static final DocumentReferenceProjectionDocumentExtractor INSTANCE = new DocumentReferenceProjectionDocumentExtractor();

	private static final Set<String> DOCUMENT_REFERENCE_FIELD_PATHS = CollectionHelper.asSet(
			LuceneFields.indexFieldName(),
			LuceneFields.idFieldName()
	);

	static DocumentReferenceProjectionDocumentExtractor get() {
		return INSTANCE;
	}

	private DocumentReferenceProjectionDocumentExtractor() {
	}

	@Override
	public void contributeCollectors(LuceneCollectorsBuilder luceneCollectorBuilder, Sort luceneSort, int maxDocs) {
		luceneCollectorBuilder.requireTopDocsCollector( luceneSort, maxDocs );
	}

	@Override
	public void contributeFields(Set<String> absoluteFieldPaths) {
		absoluteFieldPaths.addAll( DOCUMENT_REFERENCE_FIELD_PATHS );
	}

	@Override
	public void extract(ProjectionHitCollector collector, ScoreDoc scoreDoc, Document document) {
		DocumentReference documentReference = new LuceneDocumentReference(
				document.get( LuceneFields.indexFieldName() ),
				document.get( LuceneFields.idFieldName() )
		);

		collector.collectProjection( documentReference );
	}
}