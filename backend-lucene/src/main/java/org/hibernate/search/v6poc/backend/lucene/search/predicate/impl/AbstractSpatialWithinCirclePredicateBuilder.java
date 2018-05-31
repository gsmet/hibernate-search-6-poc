/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.search.predicate.impl;

import org.hibernate.search.v6poc.backend.spatial.GeoPoint;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinCirclePredicateBuilder;


public abstract class AbstractSpatialWithinCirclePredicateBuilder<T> extends AbstractSearchPredicateBuilder
		implements SpatialWithinCirclePredicateBuilder<LuceneSearchPredicateCollector> {

	protected final String absoluteFieldPath;

	protected GeoPoint center;

	protected double radiusInMeters;

	protected AbstractSpatialWithinCirclePredicateBuilder(String absoluteFieldPath) {
		this.absoluteFieldPath = absoluteFieldPath;
	}

	@Override
	public void circle(GeoPoint center, double radiusInMeters) {
		this.center = center;
		this.radiusInMeters = radiusInMeters;
	}
}
