/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.search.dsl.predicate;

import org.hibernate.search.v6poc.spatial.DistanceUnit;
import org.hibernate.search.v6poc.spatial.GeoBoundingBox;
import org.hibernate.search.v6poc.spatial.GeoPoint;
import org.hibernate.search.v6poc.spatial.GeoPolygon;
import org.hibernate.search.v6poc.spatial.ImmutableGeoBoundingBox;
import org.hibernate.search.v6poc.spatial.ImmutableGeoPoint;

/**
 * The context used when defining a spatial predicate, after at least one field was mentioned.
 *
 * @param <N> The type of the next context.
 */
public interface SpatialWithinPredicateFieldSetContext<N> extends MultiFieldPredicateFieldSetContext<SpatialWithinPredicateFieldSetContext<N>> {

	SpatialWithinCirclePredicateContext<N> circle(GeoPoint center, double radiusInMeters, DistanceUnit unit);

	default SpatialWithinCirclePredicateContext<N> circle(GeoPoint center, double radius) {
		return circle( center, radius, DistanceUnit.METERS );
	}

	default SpatialWithinCirclePredicateContext<N> circle(double latitude, double longitude, double radiusInMeters) {
		return circle( new ImmutableGeoPoint( latitude, longitude ), radiusInMeters, DistanceUnit.METERS );
	}

	default SpatialWithinCirclePredicateContext<N> circle(double latitude, double longitude, double radius, DistanceUnit unit) {
		return circle( new ImmutableGeoPoint( latitude, longitude ), radius, unit );
	}

	SpatialWithinPolygonPredicateContext<N> polygon(GeoPolygon polygon);

	SpatialWithinBoundingBoxPredicateContext<N> boundingBox(GeoBoundingBox boundingBox);

	default SpatialWithinBoundingBoxPredicateContext<N> boundingBox(double topLeftLatitude, double topLeftLongitude, double bottomRightLatitude,
			double bottomRightLongitude) {
		return boundingBox( new ImmutableGeoBoundingBox( topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude ) );
	}
}
