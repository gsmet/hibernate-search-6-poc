/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate.impl;

import java.util.Optional;
import java.util.function.Consumer;

import org.hibernate.search.v6poc.search.SearchPredicate;
import org.hibernate.search.v6poc.search.dsl.predicate.BooleanJunctionPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.MatchAllPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.MatchPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.NestedPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.RangePredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SearchPredicateContainerContext;
import org.hibernate.search.v6poc.search.dsl.predicate.SpatialPredicateContext;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateContainerContextExtension;
import org.hibernate.search.v6poc.search.dsl.predicate.spi.SearchPredicateDslContext;
import org.hibernate.search.v6poc.search.predicate.spi.SearchPredicateFactory;


public class SearchPredicateContainerContextImpl<N, CTX, C> implements SearchPredicateContainerContext<N> {

	private final SearchPredicateFactory<CTX, C> factory;

	private final SearchPredicateDslContext<N, CTX, ? extends C> dslContext;

	public SearchPredicateContainerContextImpl(SearchPredicateFactory<CTX, C> factory,
			SearchPredicateDslContext<N, CTX, ? extends C> dslContext) {
		this.factory = factory;
		this.dslContext = dslContext;
	}

	@Override
	public MatchAllPredicateContext<N> matchAll() {
		MatchAllPredicateContextImpl<N, CTX, C> child = new MatchAllPredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public BooleanJunctionPredicateContext<N> bool() {
		BooleanJunctionPredicateContextImpl<N, CTX, C> child = new BooleanJunctionPredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public N bool(Consumer<? super BooleanJunctionPredicateContext<?>> clauseContributor) {
		BooleanJunctionPredicateContext<N> context = bool();
		clauseContributor.accept( context );
		return context.end();
	}

	@Override
	public MatchPredicateContext<N> match() {
		MatchPredicateContextImpl<N, CTX, C> child = new MatchPredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public RangePredicateContext<N> range() {
		RangePredicateContextImpl<N, CTX, C> child = new RangePredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public NestedPredicateContext<N> nested() {
		NestedPredicateContextImpl<N, CTX, C> child = new NestedPredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public SpatialPredicateContext<N> spatial() {
		SpatialPredicateContextImpl<N, CTX, C> child = new SpatialPredicateContextImpl<>( factory, dslContext::getNextContext );
		dslContext.addContributor( child );
		return child;
	}

	@Override
	public N predicate(SearchPredicate predicate) {
		dslContext.addContributor( factory.toContributor( predicate ) );
		return dslContext.getNextContext();
	}

	@Override
	public <T> T withExtension(SearchPredicateContainerContextExtension<N, T> extension) {
		return extension.extendOrFail( this, factory, dslContext );
	}

	@Override
	public <T> N withExtensionOptional(
			SearchPredicateContainerContextExtension<N, T> extension, Consumer<T> clauseContributor) {
		extension.extendOptional( this, factory, dslContext ).ifPresent( clauseContributor );
		return dslContext.getNextContext();
	}

	@Override
	public <T> N withExtensionOptional(
			SearchPredicateContainerContextExtension<N, T> extension,
			Consumer<T> clauseContributor,
			Consumer<SearchPredicateContainerContext<N>> fallbackClauseContributor) {
		Optional<T> optional = extension.extendOptional( this, factory, dslContext );
		if ( optional.isPresent() ) {
			clauseContributor.accept( optional.get() );
		}
		else {
			fallbackClauseContributor.accept( this );
		}
		return dslContext.getNextContext();
	}

}
