/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.dsl;

import org.hibernate.search.v6poc.backend.document.model.dsl.IndexSchemaFieldContext;
import org.hibernate.search.v6poc.backend.document.model.dsl.IndexSchemaFieldTerminalContext;
import org.hibernate.search.v6poc.backend.lucene.document.model.LuceneFieldContributor;
import org.hibernate.search.v6poc.backend.lucene.document.model.LuceneFieldValueExtractor;


/**
 * @author Guillaume Smet
 */
public interface LuceneIndexSchemaFieldContext extends IndexSchemaFieldContext {

	/**
	 * Declares a native field, on which projection is allowed.
	 *
	 * @param fieldContributor The field contributor.
	 * @param fieldValueExtractor The field value extractor used when projecting on this field.
	 * @param <V> The type of the value.
	 * @return The DSL context.
	 */
	<V> IndexSchemaFieldTerminalContext<V> asLuceneField(LuceneFieldContributor<V> fieldContributor, LuceneFieldValueExtractor<V> fieldValueExtractor);

	/**
	 * Declares a native field on which projection is not allowed.
	 *
	 * @param fieldContributor The field contributor.
	 * @param <V> The type of the value.
	 * @return The DSL context.
	 */
	default <V> IndexSchemaFieldTerminalContext<V> asLuceneField(LuceneFieldContributor<V> fieldContributor) {
		return asLuceneField( fieldContributor, null );
	}

}
