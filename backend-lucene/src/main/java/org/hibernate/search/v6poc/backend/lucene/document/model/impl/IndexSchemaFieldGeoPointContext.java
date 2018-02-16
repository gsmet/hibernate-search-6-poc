/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import static org.hibernate.search.v6poc.backend.lucene.document.model.impl.LuceneNative.internalField;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.v6poc.backend.document.impl.DeferredInitializationIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.document.model.Store;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.spatial.GeoPoint;
import org.hibernate.search.v6poc.backend.spatial.ImmutableGeoPoint;

/**
 * @author Guillaume Smet
 */
class IndexSchemaFieldGeoPointContext extends AbstractLuceneTypedFieldModelContext<GeoPoint> {

	public IndexSchemaFieldGeoPointContext(String fieldName) {
		super( fieldName );
	}

	@Override
	protected void contribute(DeferredInitializationIndexFieldAccessor<GeoPoint> accessor, LuceneFieldModelCollector collector) {
		LuceneIndexSchemaFieldNode<GeoPoint> model = new LuceneIndexSchemaFieldNode<>( new GeoPointFieldFormatter( getFieldName(), getStore() ) );

		accessor.initialize( new LuceneIndexFieldAccessor<>( getFieldName(), model ) );

		collector.collect( getFieldName(), model );
	}

	private static final class GeoPointFieldFormatter implements LuceneFieldFormatter<GeoPoint> {

		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";

		private final Store store;

		private final String latitudeFieldName;
		private final String longitudeFieldName;


		private GeoPointFieldFormatter(String fieldName, Store store) {
			this.store = store;

			if ( Store.YES.equals( store ) ) {
				latitudeFieldName = internalField( fieldName, LATITUDE );
				longitudeFieldName = internalField( fieldName, LONGITUDE );
			}
			else {
				latitudeFieldName = null;
				longitudeFieldName = null;
			}
		}

		@Override
		public void addFields(LuceneDocumentBuilder documentBuilder, LuceneIndexSchemaFieldNode<GeoPoint> model, String fieldName, GeoPoint value) {
			if ( value == null ) {
				return;
			}

			if ( Store.YES.equals( store ) ) {
				documentBuilder.add( new StoredField( latitudeFieldName, value.getLatitude() ) );
				documentBuilder.add( new StoredField( longitudeFieldName, value.getLongitude() ) );
			}

			documentBuilder.add( new LatLonPoint( fieldName, value.getLatitude(), value.getLongitude() ) );
		}

		@Override
		public Object format(GeoPoint value) {
			throw new UnsupportedOperationException( "Not supported for now" );
		}

		@Override
		public GeoPoint parse(Document document, String fieldName) {
			IndexableField latitudeField = document.getField( latitudeFieldName );
			IndexableField longitudeField = document.getField( longitudeFieldName );

			if ( latitudeField == null || longitudeField == null ) {
				return null;
			}

			return new ImmutableGeoPoint( (double) latitudeField.numericValue(), (double) longitudeField.numericValue() );
		}
	}
}
