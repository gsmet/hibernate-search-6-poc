/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.util.impl.integrationtest.common.stub.backend.search.sort;

import org.hibernate.search.v6poc.search.dsl.sort.SortOrder;
import org.hibernate.search.v6poc.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.FieldSortBuilder;
import org.hibernate.search.v6poc.search.sort.spi.ScoreSortBuilder;

public class StubSortBuilder implements ScoreSortBuilder<StubSortBuilder>,
		FieldSortBuilder<StubSortBuilder>, DistanceSortBuilder<StubSortBuilder> {

	@Override
	public StubSortBuilder toImplementation() {
		return this;
	}

	@Override
	public void missingFirst() {
		// No-op
	}

	@Override
	public void missingLast() {
		// No-op
	}

	@Override
	public void missingAs(Object value) {
		// No-op
	}

	@Override
	public void order(SortOrder order) {
		// No-op
	}

	void simulateBuild() {
		// No-op, just simulates a call on this object
	}
}
