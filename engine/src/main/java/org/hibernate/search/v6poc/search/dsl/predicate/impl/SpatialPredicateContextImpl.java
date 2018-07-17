/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.util.function.Supplier;

import org.hibernate.search.v6poc.search.dsl.predicate.SpatialPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SpatialWithinPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;

class SpatialPredicateContextImpl<N, B> implements SpatialPredicateContext<N>, SearchPredicateContributor<B> {

	private final SearchPredicateFactory<?, B> factory;

	private final Supplier<N> nextContextProvider;

	private SearchPredicateContributor<B> child;

	SpatialPredicateContextImpl(SearchPredicateFactory<?, B> factory, Supplier<N> nextContextProvider) {
		this.factory = factory;
		this.nextContextProvider = nextContextProvider;
	}

	@Override
	public SpatialWithinPredicateContext<N> within() {
		SpatialWithinPredicateContextImpl<N, B> child = new SpatialWithinPredicateContextImpl<>( factory, nextContextProvider );
		this.child = child;
		return child;
	}

	@Override
	public B contribute() {
		return child.contribute();
	}

}
