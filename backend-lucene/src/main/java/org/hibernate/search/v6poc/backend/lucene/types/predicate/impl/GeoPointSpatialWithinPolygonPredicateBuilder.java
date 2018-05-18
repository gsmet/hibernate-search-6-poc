/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.types.predicate.impl;

import java.util.List;

import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.geo.Polygon;
import org.apache.lucene.search.Query;
import org.hibernate.search.v6poc.backend.lucene.search.predicate.impl.AbstractSpatialWithinPolygonPredicateBuilder;
import org.hibernate.search.v6poc.backend.spatial.GeoPoint;

class GeoPointSpatialWithinPolygonPredicateBuilder extends AbstractSpatialWithinPolygonPredicateBuilder<GeoPoint> {

	GeoPointSpatialWithinPolygonPredicateBuilder(String absoluteFieldPath) {
		super( absoluteFieldPath );
	}

	@Override
	protected Query buildQuery() {
		List<GeoPoint> points = polygon.getPoints();

		double[] polyLats = new double[points.size()];
		double[] polyLons = new double[points.size()];

		for ( int i = 0; i < points.size(); i++ ) {
			polyLats[i] = points.get( i ).getLatitude();
			polyLons[i] = points.get( i ).getLongitude();
		}

		Polygon lucenePolygon = new Polygon( polyLats, polyLons );

		return LatLonPoint.newPolygonQuery( absoluteFieldPath, lucenePolygon );
	}
}
