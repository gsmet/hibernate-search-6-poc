/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.model.impl;

import org.apache.lucene.document.Document;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;

/**
 * @author Guillaume Smet
 */
public interface LuceneFieldFormatter<T> {

	Object format(T value);

	void addFields(LuceneDocumentBuilder documentBuilder, LuceneIndexSchemaFieldNode<T> model, String fieldName, T value);

	T parse(Document document, String fieldName);
}
