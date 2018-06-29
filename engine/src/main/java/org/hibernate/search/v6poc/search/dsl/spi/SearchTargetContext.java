/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.spi;

import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;
import org.hibernate.search.v6poc.search.query.spi.SearchQueryFactory;
import org.hibernate.search.v6poc.search.sort.spi.SearchSortFactory;

/**
 * The target context during a search, aware of the targeted indexes and of the underlying technology (backend).
 *
 * @param <CTX> The type of the context passed to the contribution method of the predicate contributors.
 * @param <C> The type of query element collector
 */
public interface SearchTargetContext<CTX, C> {

	SearchPredicateFactory<CTX, ? super C> getSearchPredicateFactory();

	SearchSortFactory<? super C> getSearchSortFactory();

	SearchQueryFactory<C> getSearchQueryFactory();

}
