/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.sort.impl;

import java.util.function.Supplier;

import org.hibernate.search.v6poc.backend.spatial.GeoPoint;
import org.hibernate.search.v6poc.search.dsl.sort.DistanceSortContext;
import org.hibernate.search.v6poc.search.dsl.sort.SearchSortContainerContext;
import org.hibernate.search.v6poc.search.dsl.sort.SortOrder;
import org.hibernate.search.v6poc.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.SearchSortContributor;
import org.hibernate.search.v6poc.search.sort.spi.SearchSortFactory;

class DistanceSortContextImpl<N, C> implements DistanceSortContext<N>, SearchSortContributor<C> {

	private final SearchSortContainerContext<N> containerContext;
	private final Supplier<N> nextContextProvider;
	private final DistanceSortBuilder<C> builder;

	DistanceSortContextImpl(SearchSortContainerContext<N> containerContext,
			SearchSortFactory<C> factory, Supplier<N> nextContextProvider,
			String absoluteFieldPath, GeoPoint location) {
		this.containerContext = containerContext;
		this.nextContextProvider = nextContextProvider;
		this.builder = factory.distance( absoluteFieldPath, location );
	}

	@Override
	public DistanceSortContext<N> order(SortOrder order) {
		builder.order( order );
		return this;
	}

	@Override
	public SearchSortContainerContext<N> then() {
		return containerContext;
	}

	@Override
	public N end() {
		return nextContextProvider.get();
	}

	@Override
	public void contribute(C collector) {
		builder.contribute( collector );
	}
}
