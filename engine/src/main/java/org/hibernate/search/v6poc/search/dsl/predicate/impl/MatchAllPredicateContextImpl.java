/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.hibernate.search.v6poc.search.SearchPredicate;
import org.hibernate.search.v6poc.search.dsl.predicate.MatchAllPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SearchPredicateContainerContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateDslContext;
import org.hibernate.search.v6poc.search.predicate.spi.MatchAllPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.BooleanJunctionPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;


class MatchAllPredicateContextImpl<N, CTX, C>
		implements MatchAllPredicateContext<N>, SearchPredicateContributor<CTX, C> {

	private final SearchPredicateFactory<CTX, C> factory;
	private final Supplier<N> nextContextProvider;

	private final MatchAllPredicateBuilder<CTX, C> builder;
	private MatchAllExceptContext exceptContext;

	MatchAllPredicateContextImpl(SearchPredicateFactory<CTX, C> factory,
			Supplier<N> nextContextProvider) {
		this.factory = factory;
		this.nextContextProvider = nextContextProvider;
		this.builder = factory.matchAll();
	}

	@Override
	public MatchAllPredicateContext<N> boostedTo(float boost) {
		this.builder.boost( boost );
		return this;
	}

	@Override
	public MatchAllPredicateContext<N> except(SearchPredicate searchPredicate) {
		except().predicate( searchPredicate );
		return this;
	}

	@Override
	public SearchPredicateContainerContext<? extends MatchAllPredicateContext<N>> except() {
		return getExceptContext().containerContext;
	}

	@Override
	public MatchAllPredicateContext<N> except(Consumer<? super SearchPredicateContainerContext<?>> clauseContributor) {
		clauseContributor.accept( except() );
		return this;
	}

	@Override
	public void contribute(CTX context, C collector) {
		if ( exceptContext != null ) {
			BooleanJunctionPredicateBuilder<CTX, C> booleanBuilder = factory.bool();
			builder.contribute( context, booleanBuilder.getMustCollector() );
			exceptContext.contribute( context, booleanBuilder.getMustNotCollector() );
			booleanBuilder.contribute( context, collector );
		}
		else {
			builder.contribute( context, collector );
		}
	}

	@Override
	public N end() {
		return nextContextProvider.get();
	}

	private MatchAllExceptContext getExceptContext() {
		if ( exceptContext == null ) {
			exceptContext = new MatchAllExceptContext();
		}
		return exceptContext;
	}

	private class MatchAllExceptContext implements SearchPredicateDslContext<MatchAllPredicateContext<N>, CTX, C>,
			SearchPredicateContributor<CTX, C> {

		private final List<SearchPredicateContributor<CTX, ? super C>> children = new ArrayList<>();

		private final SearchPredicateContainerContextImpl<MatchAllPredicateContext<N>, CTX, C> containerContext;

		MatchAllExceptContext() {
			this.containerContext = new SearchPredicateContainerContextImpl<>(
					MatchAllPredicateContextImpl.this.factory, this );
		}

		@Override
		public void addContributor(SearchPredicateContributor<CTX, ? super C> child) {
			children.add( child );
		}

		@Override
		public MatchAllPredicateContext<N> getNextContext() {
			return MatchAllPredicateContextImpl.this;
		}

		@Override
		public void contribute(CTX context, C collector) {
			children.forEach( c -> c.contribute( context, collector ) );
		}

	}
}
