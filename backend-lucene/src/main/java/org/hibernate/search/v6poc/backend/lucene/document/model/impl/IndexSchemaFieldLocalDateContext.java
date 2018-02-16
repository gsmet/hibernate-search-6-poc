/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Locale;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.v6poc.backend.document.impl.DeferredInitializationIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.document.model.Store;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneIndexFieldAccessor;

/**
 * @author Guillaume Smet
 */
class IndexSchemaFieldLocalDateContext extends AbstractLuceneTypedFieldModelContext<LocalDate> {

	public IndexSchemaFieldLocalDateContext(String fieldName) {
		super( fieldName );
	}

	@Override
	protected void contribute(DeferredInitializationIndexFieldAccessor<LocalDate> accessor, LuceneFieldModelCollector collector) {
		LuceneIndexSchemaFieldNode<LocalDate> model = new LuceneIndexSchemaFieldNode<>( new LocalDateFieldFormatter( getStore() ) );

		accessor.initialize( new LuceneIndexFieldAccessor<>( getFieldName(), model ) );

		collector.collect( getFieldName(), model );
	}

	private static final class LocalDateFieldFormatter implements LuceneFieldFormatter<LocalDate> {

		private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
				.appendValue( YEAR, 4, 9, SignStyle.EXCEEDS_PAD )
				.appendLiteral( '-' )
				.appendValue( MONTH_OF_YEAR, 2 )
				.appendLiteral( '-' )
				.appendValue( DAY_OF_MONTH, 2 )
				.toFormatter( Locale.ROOT )
				.withResolverStyle( ResolverStyle.STRICT );

		private final Store store;

		private LocalDateFieldFormatter(Store store) {
			this.store = store;
		}

		@Override
		public void addFields(LuceneDocumentBuilder documentBuilder, LuceneIndexSchemaFieldNode<LocalDate> model, String fieldName, LocalDate value) {
			if ( value == null ) {
				return;
			}

			if ( Store.YES.equals( store ) ) {
				documentBuilder.add( new StoredField( fieldName, FORMATTER.format( value ) ) );
			}

			documentBuilder.add( new LongPoint( fieldName, value.toEpochDay() ) );
		}

		@Override
		public Object format(LocalDate value) {
			return value.toEpochDay();
		}

		@Override
		public LocalDate parse(Document document, String fieldName) {
			IndexableField field = document.getField( fieldName );

			if ( field == null ) {
				return null;
			}

			String value = field.stringValue();

			if ( value == null ) {
				return null;
			}

			return LocalDate.parse( value, FORMATTER );
		}
	}
}
