/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.util.Arrays;
import java.util.function.Supplier;

import org.hibernate.search.v6poc.search.dsl.predicate.SpatialWithinPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SpatialWithinPredicateFieldSetContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;


class SpatialWithinPredicateContextImpl<N, B> implements SpatialWithinPredicateContext<N>,
		SearchPredicateContributor<B> {

	private final SpatialWithinPredicateFieldSetContextImpl.CommonState<N, B> commonState;

	SpatialWithinPredicateContextImpl(SearchPredicateFactory<?, B> factory, Supplier<N> nextContextProvider) {
		this.commonState = new SpatialWithinPredicateFieldSetContextImpl.CommonState<>( factory, nextContextProvider );
	}

	@Override
	public SpatialWithinPredicateFieldSetContext<N> onFields(String ... absoluteFieldPaths) {
		return new SpatialWithinPredicateFieldSetContextImpl<>( commonState, Arrays.asList( absoluteFieldPaths ) );
	}

	@Override
	public B contribute() {
		return commonState.contribute();
	}
}
