/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.lang.invoke.MethodHandles;

import org.hibernate.search.v6poc.logging.impl.Log;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.v6poc.util.AssertionFailure;
import org.hibernate.search.v6poc.util.impl.common.LoggerFactory;

/**
 * An aggregator of {@link SearchPredicateContributor}, ensuring aggregated contributors are used appropriately:
 * <ul>
 *     <li>at most one predicate contributor must be added</li>
 *     <li>it must be used at most once</li>
 *     <li>new contributors cannot be added after the contributor has been used
 *     (the other constraints already prevent that, but we need a specific error message in this case)</li>
 * </ul>
 */
public final class SearchPredicateContributorAggregator<B>
		implements SearchPredicateContributor<B> {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private SearchPredicateContributor<? extends B> singlePredicateContributor;

	private boolean contributed = false;

	public void add(SearchPredicateContributor<? extends B> child) {
		if ( contributed ) {
			throw log.cannotAddPredicateToUsedContext();
		}
		if ( this.singlePredicateContributor != null ) {
			throw log.cannotAddMultiplePredicatesToQueryRoot();
		}
		this.singlePredicateContributor = child;
	}

	@Override
	public B contribute() {
		if ( contributed ) {
			// HSEARCH-3207: we must never call a contribution twice. Contributions may have side-effects.
			throw new AssertionFailure(
					"A predicate contributor was called twice. There is a bug in Hibernate Search, please report it."
			);
		}
		contributed = true;
		return singlePredicateContributor.contribute();
	}
}
