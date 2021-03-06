/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.processing.spi;

import org.hibernate.search.v6poc.backend.document.spi.DocumentState;
import org.hibernate.search.v6poc.entity.model.spi.Indexable;

/**
 * @author Yoann Rodiere
 */
public interface ValueProcessor extends AutoCloseable {

	void process(Indexable source, DocumentState target);

	@Override
	void close();

}
