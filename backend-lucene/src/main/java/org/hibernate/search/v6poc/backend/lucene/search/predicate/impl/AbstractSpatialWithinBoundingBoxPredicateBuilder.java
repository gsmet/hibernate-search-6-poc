/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.search.predicate.impl;

import org.hibernate.search.v6poc.backend.spatial.GeoBoundingBox;
import org.hibernate.search.v6poc.search.predicate.spi.SpatialWithinBoundingBoxPredicateBuilder;


public abstract class AbstractSpatialWithinBoundingBoxPredicateBuilder<T> extends AbstractSearchPredicateBuilder
		implements SpatialWithinBoundingBoxPredicateBuilder<LuceneSearchPredicateCollector> {

	protected final String absoluteFieldPath;

	protected GeoBoundingBox boundingBox;

	protected AbstractSpatialWithinBoundingBoxPredicateBuilder(String absoluteFieldPath) {
		this.absoluteFieldPath = absoluteFieldPath;
	}

	@Override
	public void boundingBox(GeoBoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
}
