/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.search.dsl.sort.impl;

import org.hibernate.search.v6poc.backend.elasticsearch.search.dsl.sort.ElasticsearchSearchSortContainerContext;
import org.hibernate.search.v6poc.backend.elasticsearch.search.sort.impl.ElasticsearchSearchSortCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.search.sort.impl.ElasticsearchSearchSortFactory;
import org.hibernate.search.v6poc.search.dsl.sort.SearchSortContainerContext;
import org.hibernate.search.v6poc.search.dsl.sort.spi.DelegatingSearchSortContainerContextImpl;
import org.hibernate.search.v6poc.search.dsl.sort.spi.SearchSortDslContext;


public class ElasticsearchSearchSortContainerContextImpl<N>
		extends DelegatingSearchSortContainerContextImpl<N>
		implements ElasticsearchSearchSortContainerContext<N> {

	private final ElasticsearchSearchSortFactory factory;

	private final SearchSortDslContext<N, ElasticsearchSearchSortCollector> dslContext;

	public ElasticsearchSearchSortContainerContextImpl(SearchSortContainerContext<N> delegate,
			ElasticsearchSearchSortFactory factory,
			SearchSortDslContext<N, ElasticsearchSearchSortCollector> dslContext) {
		super( delegate );
		this.factory = factory;
		this.dslContext = dslContext;
	}

	@Override
	public N fromJsonString(String jsonString) {
		dslContext.addContributor( factory.fromJsonString( jsonString ) );
		return dslContext.getNextContext();
	}
}