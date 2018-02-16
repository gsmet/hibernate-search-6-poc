/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import org.hibernate.search.v6poc.backend.document.model.ObjectFieldStorage;
import org.hibernate.search.v6poc.backend.document.model.spi.IndexSchemaCollector;
import org.hibernate.search.v6poc.backend.document.model.spi.IndexSchemaNestingContext;
import org.hibernate.search.v6poc.backend.document.model.spi.ObjectFieldIndexSchemaCollector;
import org.hibernate.search.v6poc.backend.lucene.document.model.LuceneIndexSchemaElement;

public class LuceneIndexSchemaCollectorImpl implements IndexSchemaCollector {

	public LuceneIndexSchemaCollectorImpl() {
	}

	@Override
	public LuceneIndexSchemaElement withContext(IndexSchemaNestingContext context) {
		/*
		 * Note: this ignores any previous nesting context, but that's alright since
		 * nesting context composition is handled in the engine.
		 */
		return new LuceneIndexSchemaElementImpl( context );
	}

	@Override
	public ObjectFieldIndexSchemaCollector objectField(String relativeName, ObjectFieldStorage storage) {
		// XXX GSM implement objectField
		throw new UnsupportedOperationException( "objectField not supported right now" );
	}

	@Override
	public void explicitRouting() {
		// XXX GSM support explicit routing?
		throw new UnsupportedOperationException( "objectField not supported right now" );
	}
}
