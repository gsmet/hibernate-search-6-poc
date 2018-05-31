/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.spatial;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GeoPolygonTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void validPolygon() {
		GeoPolygon polygon = new ImmutableGeoPolygon( new ImmutableGeoPoint( 26, 23 ), new ImmutableGeoPoint( 26, 26 ), new ImmutableGeoPoint( 24, 26 ),
				new ImmutableGeoPoint( 24, 23 ), new ImmutableGeoPoint( 26, 23 ) );
		assertNotNull( polygon );

		polygon = new ImmutableGeoPolygon( Arrays.asList( new ImmutableGeoPoint( 26, 23 ), new ImmutableGeoPoint( 26, 26 ), new ImmutableGeoPoint( 24, 26 ),
				new ImmutableGeoPoint( 24, 23 ), new ImmutableGeoPoint( 26, 23 ) ) );
		assertNotNull( polygon );
	}

	@Test
	public void invalidPolygon() {
		try {
			new ImmutableGeoPolygon( new ImmutableGeoPoint( 26, 23 ), new ImmutableGeoPoint( 26, 26 ), new ImmutableGeoPoint( 24, 26 ),
					new ImmutableGeoPoint( 24, 23 ) );
		}
		catch (Exception e) {
			Assertions.assertThat( e )
					.isInstanceOf( IllegalArgumentException.class )
					.hasMessageContaining( "HSEARCH000016" );
		}

		try {
			new ImmutableGeoPolygon( Arrays.asList( new ImmutableGeoPoint( 26, 23 ), new ImmutableGeoPoint( 26, 26 ), new ImmutableGeoPoint( 24, 26 ),
					new ImmutableGeoPoint( 24, 23 ) ) );
		}
		catch (Exception e) {
			Assertions.assertThat( e )
					.isInstanceOf( IllegalArgumentException.class )
					.hasMessageContaining( "HSEARCH000016" );
		}
	}
}
