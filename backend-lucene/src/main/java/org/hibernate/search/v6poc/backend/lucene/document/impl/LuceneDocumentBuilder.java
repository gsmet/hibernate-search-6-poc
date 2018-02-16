/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.document.impl;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.v6poc.backend.document.DocumentElement;

/**
 * @author Guillaume Smet
 */
public class LuceneDocumentBuilder implements DocumentElement {

	private final Document document = new Document();

	public void add(IndexableField field) {
		document.add( field );
	}

	public Document build() {
		return document;
	}
}
