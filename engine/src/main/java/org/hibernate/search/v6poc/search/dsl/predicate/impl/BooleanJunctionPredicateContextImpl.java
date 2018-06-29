/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.hibernate.search.v6poc.search.SearchPredicate;
import org.hibernate.search.v6poc.search.dsl.predicate.BooleanJunctionPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.MinimumShouldMatchContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SearchPredicateContainerContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateDslContext;
import org.hibernate.search.v6poc.search.predicate.spi.BooleanJunctionPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;


class BooleanJunctionPredicateContextImpl<N, CTX, C>
		implements BooleanJunctionPredicateContext<N>, SearchPredicateContributor<CTX, C> {

	private final SearchPredicateFactory<CTX, C> factory;

	private final Supplier<N> nextContextProvider;

	private final BooleanJunctionPredicateBuilder<CTX, C> builder;

	private final MinimumShouldMatchContextImpl<BooleanJunctionPredicateContext<N>> minimumShouldMatchContext;

	private final OccurContext must;
	private final OccurContext mustNot;
	private final OccurContext should;
	private final OccurContext filter;

	BooleanJunctionPredicateContextImpl(SearchPredicateFactory<CTX, C> factory,
			Supplier<N> nextContextProvider) {
		this.factory = factory;
		this.nextContextProvider = nextContextProvider;
		this.builder = factory.bool();
		this.must = new OccurContext( builder.getMustCollector() );
		this.mustNot = new OccurContext( builder.getMustNotCollector() );
		this.should = new OccurContext( builder.getShouldCollector() );
		this.filter = new OccurContext( builder.getFilterCollector() );
		this.minimumShouldMatchContext = new MinimumShouldMatchContextImpl<>( builder, this );
	}

	@Override
	public BooleanJunctionPredicateContext<N> boostedTo(float boost) {
		builder.boost( boost );
		return this;
	}

	@Override
	public BooleanJunctionPredicateContext<N> must(SearchPredicate searchPredicate) {
		return must().predicate( searchPredicate );
	}

	@Override
	public BooleanJunctionPredicateContext<N> mustNot(SearchPredicate searchPredicate) {
		return mustNot().predicate( searchPredicate );
	}

	@Override
	public BooleanJunctionPredicateContext<N> should(SearchPredicate searchPredicate) {
		return should().predicate( searchPredicate );
	}

	@Override
	public BooleanJunctionPredicateContext<N> filter(SearchPredicate searchPredicate) {
		return filter().predicate( searchPredicate );
	}

	@Override
	public SearchPredicateContainerContext<BooleanJunctionPredicateContext<N>> must() {
		return must.containerContext;
	}

	@Override
	public SearchPredicateContainerContext<BooleanJunctionPredicateContext<N>> mustNot() {
		return mustNot.containerContext;
	}

	@Override
	public SearchPredicateContainerContext<BooleanJunctionPredicateContext<N>> should() {
		return should.containerContext;
	}

	@Override
	public SearchPredicateContainerContext<BooleanJunctionPredicateContext<N>> filter() {
		return filter.containerContext;
	}

	@Override
	public BooleanJunctionPredicateContext<N> must(Consumer<? super SearchPredicateContainerContext<?>> clauseContributor) {
		clauseContributor.accept( must() );
		return this;
	}

	@Override
	public BooleanJunctionPredicateContext<N> mustNot(Consumer<? super SearchPredicateContainerContext<?>> clauseContributor) {
		clauseContributor.accept( mustNot() );
		return this;
	}

	@Override
	public BooleanJunctionPredicateContext<N> should(Consumer<? super SearchPredicateContainerContext<?>> clauseContributor) {
		clauseContributor.accept( should() );
		return this;
	}

	@Override
	public BooleanJunctionPredicateContext<N> filter(Consumer<? super SearchPredicateContainerContext<?>> clauseContributor) {
		clauseContributor.accept( filter() );
		return this;
	}

	@Override
	public MinimumShouldMatchContext<? extends BooleanJunctionPredicateContext<N>> minimumShouldMatch() {
		return minimumShouldMatchContext;
	}

	@Override
	public BooleanJunctionPredicateContext<N> minimumShouldMatch(
			Consumer<? super MinimumShouldMatchContext<?>> constraintContributor) {
		constraintContributor.accept( minimumShouldMatchContext );
		return this;
	}

	@Override
	public void contribute(CTX context, C collector) {
		builder.contribute( context, collector );
	}

	@Override
	public N end() {
		return nextContextProvider.get();
	}

	private class OccurContext implements SearchPredicateDslContext<BooleanJunctionPredicateContext<N>, CTX, C> {

		private final Consumer<SearchPredicateContributor<CTX, ? super C>> contributorConsumer;

		private final SearchPredicateContainerContextImpl<BooleanJunctionPredicateContext<N>, CTX, C> containerContext;

		OccurContext(Consumer<SearchPredicateContributor<CTX, ? super C>> contributorConsumer) {
			this.contributorConsumer = contributorConsumer;
			this.containerContext = new SearchPredicateContainerContextImpl<>(
					BooleanJunctionPredicateContextImpl.this.factory, this );
		}

		@Override
		public void addContributor(SearchPredicateContributor<CTX, ? super C> child) {
			contributorConsumer.accept( child );
		}

		@Override
		public BooleanJunctionPredicateContext<N> getNextContext() {
			return BooleanJunctionPredicateContextImpl.this;
		}
	}

}
