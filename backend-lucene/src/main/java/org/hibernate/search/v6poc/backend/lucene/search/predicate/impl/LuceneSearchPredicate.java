/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.search.predicate.impl;

import org.hibernate.search.v6poc.search.SearchPredicate;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateContributor;

class LuceneSearchPredicate
		implements SearchPredicate, SearchPredicateContributor<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> {

	private final SearchPredicateContributor<LuceneSearchPredicateContext, ? super LuceneSearchPredicateCollector> contributor;

	LuceneSearchPredicate(SearchPredicateContributor<LuceneSearchPredicateContext, ? super LuceneSearchPredicateCollector> contributor) {
		this.contributor = contributor;
	}

	@Override
	public void contribute(LuceneSearchPredicateContext context, LuceneSearchPredicateCollector collector) {
		LuceneSearchPredicateQueryBuilder predicateQueryBuilder = new LuceneSearchPredicateQueryBuilder();
		contributor.contribute( context, predicateQueryBuilder );
		collector.collectPredicate( predicateQueryBuilder.build() );
	}

}
