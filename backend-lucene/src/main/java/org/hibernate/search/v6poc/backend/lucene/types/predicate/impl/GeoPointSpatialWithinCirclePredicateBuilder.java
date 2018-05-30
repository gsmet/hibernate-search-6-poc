/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.types.predicate.impl;

import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.search.Query;
import org.hibernate.search.v6poc.backend.lucene.search.predicate.impl.AbstractSpatialWithinCirclePredicateBuilder;
import org.hibernate.search.v6poc.spatial.GeoPoint;

class GeoPointSpatialWithinCirclePredicateBuilder extends AbstractSpatialWithinCirclePredicateBuilder<GeoPoint> {

	GeoPointSpatialWithinCirclePredicateBuilder(String absoluteFieldPath) {
		super( absoluteFieldPath );
	}

	@Override
	protected Query buildQuery() {
		return LatLonPoint.newDistanceQuery( absoluteFieldPath, center.getLatitude(), center.getLongitude(), radiusInMeters );
	}
}
