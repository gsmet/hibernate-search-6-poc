/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.v6poc.backend.document.impl.DeferredInitializationIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.document.model.Store;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneIndexFieldAccessor;

/**
 * @author Guillaume Smet
 */
class IndexSchemaFieldIntegerContext extends AbstractLuceneTypedFieldModelContext<Integer> {

	public IndexSchemaFieldIntegerContext(String fieldName) {
		super( fieldName );
	}

	@Override
	protected void contribute(DeferredInitializationIndexFieldAccessor<Integer> accessor, LuceneFieldModelCollector collector) {
		LuceneIndexSchemaFieldNode<Integer> model = new LuceneIndexSchemaFieldNode<>( new IntegerFieldFormatter( getStore() ) );

		accessor.initialize( new LuceneIndexFieldAccessor<>( getFieldName(), model ) );

		collector.collect( getFieldName(), model );
	}

	private static final class IntegerFieldFormatter implements LuceneFieldFormatter<Integer> {

		private final Store store;

		private IntegerFieldFormatter(Store store) {
			this.store = store;
		}

		@Override
		public void addFields(LuceneDocumentBuilder documentBuilder, LuceneIndexSchemaFieldNode<Integer> model, String fieldName, Integer value) {
			if ( value == null ) {
				return;
			}

			if ( Store.YES.equals( store ) ) {
				documentBuilder.add( new StoredField( fieldName, value ) );
			}

			documentBuilder.add( new IntPoint( fieldName, value ) );
		}

		@Override
		public Object format(Integer value) {
			return value;
		}

		@Override
		public Integer parse(Document document, String fieldName) {
			IndexableField field = document.getField( fieldName );

			if ( field == null ) {
				return null;
			}

			return (Integer) field.numericValue();
		}
	}
}
