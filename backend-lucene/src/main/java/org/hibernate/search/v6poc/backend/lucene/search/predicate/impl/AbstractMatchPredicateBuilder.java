/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.search.predicate.impl;

import org.hibernate.search.v6poc.backend.lucene.types.formatter.impl.LuceneFieldFormatter;
import org.hibernate.search.v6poc.search.predicate.spi.MatchPredicateBuilder;


public abstract class AbstractMatchPredicateBuilder<T> extends AbstractSearchPredicateBuilder
		implements MatchPredicateBuilder<LuceneSearchPredicateBuilder> {

	protected final String absoluteFieldPath;

	private final LuceneFieldFormatter<T> formatter;

	protected T value;

	protected AbstractMatchPredicateBuilder(String absoluteFieldPath, LuceneFieldFormatter<T> formatter) {
		this.absoluteFieldPath = absoluteFieldPath;
		this.formatter = formatter;
	}

	@Override
	public void value(Object value) {
		this.value = formatter.format( value );
	}
}
