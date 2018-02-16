/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.index.impl;

import org.hibernate.search.v6poc.backend.document.model.spi.IndexSchemaCollector;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneIndexModel;
import org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneIndexSchemaCollectorImpl;
import org.hibernate.search.v6poc.backend.lucene.impl.LuceneLocalDirectoryBackend;
import org.hibernate.search.v6poc.backend.lucene.work.impl.LuceneWork;
import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;
import org.hibernate.search.v6poc.engine.spi.BuildContext;

/**
 * @author Yoann Rodiere
 */
public class LuceneLocalDirectoryIndexManagerBuilder implements IndexManagerBuilder<LuceneDocumentBuilder> {

	private final LuceneLocalDirectoryBackend backend;
	private final String indexName;
	private final BuildContext context;
	private final ConfigurationPropertySource propertySource;

	private final LuceneIndexSchemaCollectorImpl collector = new LuceneIndexSchemaCollectorImpl();

	public LuceneLocalDirectoryIndexManagerBuilder(LuceneLocalDirectoryBackend backend, String indexName,
			BuildContext context, ConfigurationPropertySource propertySource) {
		this.backend = backend;
		this.indexName = indexName;
		this.context = context;
		this.propertySource = propertySource;
	}

	@Override
	public IndexSchemaCollector getSchemaCollector() {
		return collector;
	}

	@Override
	public LuceneLocalDirectoryIndexManager build() {
		LuceneIndexModel model = new LuceneIndexModel( indexName, collector );

		// TODO make sure index initialization is performed in parallel for all indexes?
		LuceneWork<?> work = backend.getWorkFactory().createIndex( indexName );
		backend.getStreamOrchestrator().submit( work );

		return new LuceneLocalDirectoryIndexManager( backend, indexName, model );
	}
}
