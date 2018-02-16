/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import java.lang.invoke.MethodHandles;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.util.BytesRef;
import org.hibernate.search.v6poc.backend.document.impl.DeferredInitializationIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.document.model.IndexSchemaFieldTypedContext;
import org.hibernate.search.v6poc.backend.document.model.Sortable;
import org.hibernate.search.v6poc.backend.document.model.Store;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneIndexFieldAccessor;
import org.hibernate.search.v6poc.backend.lucene.logging.impl.Log;
import org.hibernate.search.v6poc.backend.lucene.util.impl.AnalyzerUtils;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * @author Guillaume Smet
 */
class IndexSchemaFieldStringContext extends AbstractLuceneTypedFieldModelContext<String> {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private static final Analyzer STANDARD_ANALYZER = new StandardAnalyzer();

	private Analyzer analyzer;

	private Analyzer normalizer;

	public IndexSchemaFieldStringContext(String fieldName) {
		super( fieldName );
	}

	@Override
	protected void contribute(DeferredInitializationIndexFieldAccessor<String> accessor, LuceneFieldModelCollector collector) {
		if ( Sortable.YES.equals( getSortable() ) && getAnalyzer() != null ) {
			throw log.cannotUseAnalyzerOnSortableField( getFieldName() );
		}

		LuceneIndexSchemaFieldNode<String> model = new LuceneIndexSchemaFieldNode<>( new StringFieldFormatter(
				getSortable(),
				getFieldType( getStore(), analyzer != null ),
				normalizer
		) );

		accessor.initialize( new LuceneIndexFieldAccessor<>( getFieldName(), model ) );

		collector.collect( getFieldName(), model );
	}

	@Override
	public IndexSchemaFieldTypedContext<String> analyzer(String analyzerName) {
		if ( !"default".equals( analyzerName ) ) {
			throw new UnsupportedOperationException( "For now, only the default analyzer is supported by the Lucene backend." );
		}
		this.analyzer = STANDARD_ANALYZER;
		return this;
	}

	@Override
	public IndexSchemaFieldTypedContext<String> normalizer(String normalizerName) {
		throw new UnsupportedOperationException( "For now, normalizers are not supported by the Lucene backend." );
	}

	@Override
	protected Analyzer getAnalyzer() {
		return analyzer;
	}

	@Override
	protected Analyzer getNormalizer() {
		return normalizer;
	}

	private static FieldType getFieldType(Store store, boolean analyzed) {
		FieldType fieldType = new FieldType();
		if ( analyzed ) {
			// XXX GSM: take into account the norms and term vectors options
			fieldType.setOmitNorms( false );
			fieldType.setIndexOptions( IndexOptions.DOCS_AND_FREQS_AND_POSITIONS );
			fieldType.setStoreTermVectors( true );
			fieldType.setStoreTermVectorPositions( true );
			fieldType.setStoreTermVectorOffsets( true );
			fieldType.setTokenized( true );
		}
		else {
			fieldType.setOmitNorms( true );
			fieldType.setIndexOptions( IndexOptions.DOCS );
			fieldType.setTokenized( false );
		}
		fieldType.setStored( Store.YES.equals( store ) );
		fieldType.freeze();

		return fieldType;
	}

	private static final class StringFieldFormatter implements LuceneFieldFormatter<String> {

		private final Sortable sortable;

		private final FieldType fieldType;

		private final Analyzer normalizer;

		private StringFieldFormatter(Sortable sortable, FieldType fieldType, Analyzer normalizer) {
			this.sortable = sortable;
			this.fieldType = fieldType;
			this.normalizer = normalizer;
		}

		@Override
		public void addFields(LuceneDocumentBuilder documentBuilder, LuceneIndexSchemaFieldNode<String> model, String fieldName, String value) {
			if ( value == null ) {
				return;
			}

			documentBuilder.add( new Field( fieldName, value, fieldType ) );

			if ( Sortable.YES.equals( sortable ) ) {
				documentBuilder.add( new SortedDocValuesField(
						fieldName,
						new BytesRef( normalizer != null ? AnalyzerUtils.analyzeSortableValue( normalizer, fieldName, value ) : value )
				) );
			}
		}

		@Override
		public String format(String value) {
			return value;
		}

		@Override
		public String parse(Document document, String fieldName) {
			return document.get( fieldName );
		}
	}
}
