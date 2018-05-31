/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.types.predicate.impl;

import org.hibernate.search.v6poc.backend.elasticsearch.search.predicate.impl.ElasticsearchSearchPredicateCollector;
import org.hibernate.search.v6poc.search.predicate.spi.MatchPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.RangePredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinBoundingBoxPredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinCirclePredicateBuilder;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinPolygonPredicateBuilder;

public interface ElasticsearchFieldPredicateBuilderFactory {

	MatchPredicateBuilder<ElasticsearchSearchPredicateCollector> createMatchPredicateBuilder(String absoluteFieldPath);

	RangePredicateBuilder<ElasticsearchSearchPredicateCollector> createRangePredicateBuilder(String absoluteFieldPath);

	SpatialWithinCirclePredicateBuilder<ElasticsearchSearchPredicateCollector> createSpatialWithinCirclePredicateBuilder(String absoluteFieldPath);

	SpatialWithinPolygonPredicateBuilder<ElasticsearchSearchPredicateCollector> createSpatialWithinPolygonPredicateBuilder(String absoluteFieldPath);

	SpatialWithinBoundingBoxPredicateBuilder<ElasticsearchSearchPredicateCollector> createSpatialWithinBoundingBoxPredicateBuilder(String absoluteFieldPath);
}
