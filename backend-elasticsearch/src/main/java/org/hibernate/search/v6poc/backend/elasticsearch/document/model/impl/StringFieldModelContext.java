/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl;

import org.hibernate.search.v6poc.backend.document.model.Store;
import org.hibernate.search.v6poc.backend.document.model.spi.TypedFieldModelContext;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.DeferredInitializationIndexFieldReference;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchIndexFieldReference;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.DataType;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.FieldDataType;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl.esnative.PropertyMapping;
import org.hibernate.search.v6poc.backend.elasticsearch.gson.impl.UnknownTypeJsonAccessor;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

/**
 * @author Yoann Rodiere
 */
class StringFieldModelContext extends AbstractElasticsearchTypedFieldModelContext<String> {

	private final UnknownTypeJsonAccessor accessor;
	private Store store;

	public StringFieldModelContext(UnknownTypeJsonAccessor accessor) {
		this.accessor = accessor;
	}

	@Override
	public TypedFieldModelContext<String> store(Store store) {
		this.store = store;
		return this;
	}

	@Override
	protected void contribute(DeferredInitializationIndexFieldReference<String> reference, PropertyMapping mapping,
			ElasticsearchFieldModelCollector collector) {
		ElasticsearchFieldFormatter formatter = StringFieldFormatter.INSTANCE;
		reference.initialize( new ElasticsearchIndexFieldReference<>( accessor, formatter ) );
		// TODO auto-select type, or use sub-fields (but in that case, adjust projections accordingly)
		if ( false ) {
			mapping.setType( DataType.TEXT );
			if ( store != null && Store.NO.equals( store ) ) {
				// TODO what about Store.COMPRESS?
				mapping.setFieldData( FieldDataType.TRUE );
			}
		}
		else {
			mapping.setType( DataType.KEYWORD );
			if ( store != null && Store.NO.equals( store ) ) {
				// TODO what about Store.COMPRESS?
				mapping.setStore( true );
			}
		}

		String absolutePath = accessor.getStaticAbsolutePath();
		ElasticsearchFieldModel model = new ElasticsearchFieldModel( formatter );
		collector.collect( absolutePath, model );
	}

	private static final class StringFieldFormatter implements ElasticsearchFieldFormatter {
		// Must be a singleton so that equals() works as required by the interface
		public static final StringFieldFormatter INSTANCE = new StringFieldFormatter();

		private StringFieldFormatter() {
		}

		@Override
		public JsonElement format(Object object) {
			if ( object == null ) {
				return JsonNull.INSTANCE;
			}
			String value = (String) object;
			return new JsonPrimitive( value );
		}
	}
}
