/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.document.model.dsl.spi;

import org.hibernate.search.v6poc.util.EventContext;
import org.hibernate.search.v6poc.util.SearchException;

public interface IndexSchemaContext {

	/**
	 * @return A list of failure context elements to be passed to the constructor of any
	 * {@link SearchException} occurring in this context.
	 */
	EventContext getEventContext();

}
