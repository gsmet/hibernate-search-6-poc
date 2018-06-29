/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.search.query.impl;

import org.hibernate.search.v6poc.backend.elasticsearch.search.impl.ElasticsearchSearchQueryElementCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.search.impl.ElasticsearchSearchTargetModel;
import org.hibernate.search.v6poc.backend.elasticsearch.search.predicate.impl.ElasticsearchSearchPredicateCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.search.predicate.impl.SearchPredicateFactoryImpl;
import org.hibernate.search.v6poc.backend.elasticsearch.search.sort.impl.ElasticsearchSearchSortCollector;
import org.hibernate.search.v6poc.backend.elasticsearch.search.sort.impl.SearchSortFactoryImpl;
import org.hibernate.search.v6poc.search.dsl.spi.SearchTargetContext;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;
import org.hibernate.search.v6poc.search.query.spi.SearchQueryFactory;
import org.hibernate.search.v6poc.search.sort.spi.SearchSortFactory;

public class ElasticsearchSearchTargetContext
		implements SearchTargetContext<Void, ElasticsearchSearchQueryElementCollector> {

	private final SearchPredicateFactory<Void, ElasticsearchSearchPredicateCollector> searchPredicateFactory;
	private final SearchSortFactory<ElasticsearchSearchSortCollector> searchSortFactory;
	private final SearchQueryFactory<ElasticsearchSearchQueryElementCollector> searchQueryFactory;

	public ElasticsearchSearchTargetContext(SearchBackendContext searchBackendContext,
			ElasticsearchSearchTargetModel searchTargetModel) {
		this.searchPredicateFactory = new SearchPredicateFactoryImpl( searchTargetModel );
		this.searchSortFactory = new SearchSortFactoryImpl( searchTargetModel );
		this.searchQueryFactory = new SearchQueryFactoryImpl( searchBackendContext, searchTargetModel );
	}

	@Override
	public SearchPredicateFactory<Void, ElasticsearchSearchPredicateCollector> getSearchPredicateFactory() {
		return searchPredicateFactory;
	}

	@Override
	public SearchSortFactory<ElasticsearchSearchSortCollector> getSearchSortFactory() {
		return searchSortFactory;
	}

	@Override
	public SearchQueryFactory<ElasticsearchSearchQueryElementCollector> getSearchQueryFactory() {
		return searchQueryFactory;
	}
}
