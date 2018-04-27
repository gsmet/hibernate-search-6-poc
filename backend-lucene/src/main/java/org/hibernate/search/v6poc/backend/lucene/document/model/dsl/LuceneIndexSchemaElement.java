/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.dsl;

import org.hibernate.search.v6poc.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.v6poc.backend.document.model.dsl.ObjectFieldStorage;

/**
 * @author Guillaume Smet
 */
public interface LuceneIndexSchemaElement extends IndexSchemaElement {

	@Override
	default LuceneIndexSchemaObjectField objectField(String relativeFieldName) {
		return objectField( relativeFieldName, ObjectFieldStorage.DEFAULT );
	}

	@Override
	LuceneIndexSchemaObjectField objectField(String relativeFieldName, ObjectFieldStorage storage);
}
