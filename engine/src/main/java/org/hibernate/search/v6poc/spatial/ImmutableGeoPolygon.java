/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.spatial;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.search.v6poc.logging.impl.Log;
import org.hibernate.search.v6poc.util.impl.common.CollectionHelper;
import org.hibernate.search.v6poc.util.impl.common.Contracts;
import org.hibernate.search.v6poc.util.impl.common.LoggerFactory;

public class ImmutableGeoPolygon implements GeoPolygon {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private List<GeoPoint> points;

	public ImmutableGeoPolygon(List<GeoPoint> points) {
		Contracts.assertNotNull( points, "points" );

		GeoPoint firstPoint = points.get( 0 );
		GeoPoint lastPoint = points.get( points.size() - 1 );
		if ( !firstPoint.equals( lastPoint ) ) {
			throw log.invalidGeoPolygonFirstPointNotIdenticalToLastPoint( firstPoint, lastPoint );
		}

		this.points = CollectionHelper.toImmutableList( new ArrayList<>( points ) );
	}

	public ImmutableGeoPolygon(GeoPoint firstPoint, GeoPoint secondPoint, GeoPoint thirdPoint, GeoPoint fourthPoint, GeoPoint... additionalPoints) {
		Contracts.assertNotNull( firstPoint, "firstPoint" );
		Contracts.assertNotNull( secondPoint, "secondPoint" );
		Contracts.assertNotNull( thirdPoint, "thirdPoint" );
		Contracts.assertNotNull( fourthPoint, "fourthPoint" );

		List<GeoPoint> points = new ArrayList<>();
		points.add( firstPoint );
		points.add( secondPoint );
		points.add( thirdPoint );
		points.add( fourthPoint );
		Collections.addAll( points, additionalPoints );

		GeoPoint lastPoint = points.get( points.size() - 1 );
		if ( !firstPoint.equals( lastPoint ) ) {
			throw log.invalidGeoPolygonFirstPointNotIdenticalToLastPoint( firstPoint, lastPoint );
		}

		this.points = CollectionHelper.toImmutableList( points );
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
