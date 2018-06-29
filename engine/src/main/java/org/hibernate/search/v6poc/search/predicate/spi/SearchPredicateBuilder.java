/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.predicate.spi;

public interface SearchPredicateBuilder<CTX, C> extends SearchPredicateContributor<CTX, C> {

	void boost(float boost);

	/**
	 * Contribute exactly one predicate to the collector (no more, no less).
	 *
	 * @param context The context in which the predicates are registered.
	 * @param collector A collector to contribute to.
	 */
	@Override
	void contribute(CTX context, C collector);
}
