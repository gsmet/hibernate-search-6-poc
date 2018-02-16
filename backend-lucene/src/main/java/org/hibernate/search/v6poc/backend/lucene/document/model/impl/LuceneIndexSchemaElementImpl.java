/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import org.hibernate.search.v6poc.backend.document.model.IndexSchemaFieldContext;
import org.hibernate.search.v6poc.backend.document.model.ObjectFieldStorage;
import org.hibernate.search.v6poc.backend.document.model.spi.IndexSchemaNestingContext;
import org.hibernate.search.v6poc.backend.lucene.document.model.LuceneIndexSchemaElement;
import org.hibernate.search.v6poc.backend.lucene.document.model.LuceneIndexSchemaObjectField;

/**
 * @author Yoann Rodiere
 * @author Guillaume Smet
 */
class LuceneIndexSchemaElementImpl implements LuceneIndexSchemaElement {

	private final IndexSchemaNestingContext nestingContext;

	LuceneIndexSchemaElementImpl(IndexSchemaNestingContext nestingContext) {
		this.nestingContext = nestingContext;
	}

	@Override
	public String toString() {
		return new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "nestingContext=" ).append( nestingContext )
				.append( "]" )
				.toString();
	}

	@Override
	public IndexSchemaFieldContext field(String relativeName) {
		return nestingContext.nest(
				relativeName,
				// If the field is included
				prefixedName -> {
					LuceneIndexSchemaFieldContextImpl fieldContext = new LuceneIndexSchemaFieldContextImpl( prefixedName );
					return fieldContext;
				},
				// If the field is filtered out
				LuceneIndexSchemaFieldContextImpl::new
		);
	}

	@Override
	public LuceneIndexSchemaObjectField objectField(String relativeName, ObjectFieldStorage storage) {
		// XXX GSM implement objectField
		throw new UnsupportedOperationException( "objectField not supported right now" );
	}
}
