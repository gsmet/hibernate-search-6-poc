/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.types.predicate.impl;

import org.hibernate.search.v6poc.backend.lucene.search.predicate.impl.LuceneSearchPredicateCollector;
import org.hibernate.search.v6poc.backend.lucene.search.predicate.impl.LuceneSearchPredicateContext;
import org.hibernate.search.v6poc.search.predicate.spi.MatchPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.RangePredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinBoundingBoxPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinCirclePredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinPolygonPredicateBuilder;

/**
 * @author Guillaume Smet
 */
public interface LuceneFieldPredicateBuilderFactory {

	MatchPredicateBuilder<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> createMatchPredicateBuilder(String absoluteFieldPath);

	RangePredicateBuilder<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> createRangePredicateBuilder(String absoluteFieldPath);

	SpatialWithinCirclePredicateBuilder<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> createSpatialWithinCirclePredicateBuilder(
			String absoluteFieldPath);

	SpatialWithinPolygonPredicateBuilder<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> createSpatialWithinPolygonPredicateBuilder(
			String absoluteFieldPath);

	SpatialWithinBoundingBoxPredicateBuilder<LuceneSearchPredicateContext, LuceneSearchPredicateCollector> createSpatialWithinBoundingBoxPredicateBuilder(
			String absoluteFieldPath);

	// equals()/hashCode() needs to be implemented if the predicate factory is not a singleton

	boolean equals(Object obj);

	int hashCode();
}
