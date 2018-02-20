/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import org.apache.lucene.search.Query;

/**
 * @author Guillaume Smet
 */
public interface LuceneFieldQueryFactory {

	Query createMatchQuery(String fieldName, Object value, MatchQueryOptions matchQueryOptions);

	Query createRangeQuery(String fieldName, Object lowerLimit, Object upperLimit, RangeQueryOptions rangeQueryOptions);
}
