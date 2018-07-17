/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.util.impl.integrationtest.common.stub.backend.search.sort;

import java.util.List;
import java.util.function.Consumer;

import org.hibernate.search.v6poc.util.impl.integrationtest.common.stub.backend.search.StubQueryElementCollector;
import org.hibernate.search.v6poc.search.SearchSort;
import org.hibernate.search.v6poc.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.FieldSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.ScoreSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.SearchSortFactory;
import org.hibernate.search.v6poc.spatial.GeoPoint;

public class StubSearchSortFactory implements SearchSortFactory<StubQueryElementCollector, StubSortBuilder> {

	@Override
	public SearchSort toSearchSort(List<StubSortBuilder> builders) {
		return new StubSearchSort( builders );
	}

	@Override
	public void toImplementation(SearchSort sort, Consumer<StubSortBuilder> implementationConsumer) {
		((StubSearchSort) sort).getBuilders().forEach( implementationConsumer );
	}

	@Override
	public void contribute(StubQueryElementCollector collector, List<StubSortBuilder> builders) {
		builders.forEach( StubSortBuilder::simulateBuild );
		collector.simulateCollectCall();
	}

	@Override
	public ScoreSortBuilder<StubSortBuilder> score() {
		return new StubSortBuilder();
	}

	@Override
	public FieldSortBuilder<StubSortBuilder> field(String absoluteFieldPath) {
		return new StubSortBuilder();
	}

	@Override
	public StubSortBuilder indexOrder() {
		return new StubSortBuilder();
	}

	@Override
	public DistanceSortBuilder<StubSortBuilder> distance(String absoluteFieldPath, GeoPoint location) {
		return new StubSortBuilder();
	}
}
