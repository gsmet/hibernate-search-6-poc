/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import org.hibernate.search.v6poc.search.dsl.predicate.NestedPredicateFieldContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SearchPredicateContainerContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.DelegatingSearchPredicateContainerContextImpl;
import org.hibernate.search.v6poc.search.predicate.spi.NestedPredicateBuilder;

class NestedPredicateFieldContextImpl<N, B> extends DelegatingSearchPredicateContainerContextImpl<N>
		implements NestedPredicateFieldContext<N> {

	private final NestedPredicateBuilder<B> builder;

	NestedPredicateFieldContextImpl(SearchPredicateContainerContext<N> containerContext, NestedPredicateBuilder<B> builder) {
		super( containerContext );
		this.builder = builder;
	}
}
