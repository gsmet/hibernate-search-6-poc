/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

public class LuceneNative {

	private static final String INTERNAL_FIELD_PREFIX = "__HSEARCH_";

	private LuceneNative() {
	}

	public static String internalField(String fieldName, String suffix) {
		StringBuilder sb = new StringBuilder();
		sb.append( INTERNAL_FIELD_PREFIX );
		sb.append( fieldName );
		sb.append( '_' );
		sb.append( suffix );
		return sb.toString();
	}
}
