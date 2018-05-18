/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.spatial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImmutableGeoPolygon implements GeoPolygon {

	private List<GeoPoint> points = new ArrayList<>();

	public ImmutableGeoPolygon(List<GeoPoint> points) {
		this.points.addAll( points );
	}

	public ImmutableGeoPolygon(GeoPoint firstPoint, GeoPoint secondPoint, GeoPoint thirdPoint, GeoPoint fourthPoint, GeoPoint... additionalPoints) {
		points.add( firstPoint );
		points.add( secondPoint );
		points.add( thirdPoint );
		points.add( fourthPoint );
		points.addAll( Arrays.asList( additionalPoints ) );
	}

	@Override
	public List<GeoPoint> getPoints() {
		return points;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null || getClass() != obj.getClass() ) {
			return false;
		}

		ImmutableGeoPolygon that = (ImmutableGeoPolygon) obj;

		if ( !that.points.equals( points ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return points.hashCode();
	}

	@Override
	public String toString() {
		return "GeoPolygon [points=" + points + "]";
	}
}
