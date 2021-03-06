/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping;

import org.hibernate.search.v6poc.engine.SearchManager;
import org.hibernate.search.v6poc.entity.pojo.search.PojoReference;
import org.hibernate.search.v6poc.search.spi.SearchResultDefinitionContext;


/**
 * @author Yoann Rodiere
 */
public interface PojoSearchManager extends SearchManager {

	SearchResultDefinitionContext<PojoReference> search(Class<?> ... types);

	/**
	 * @return The worker for this manager. Calling {@link ChangesetPojoWorker#execute()}
	 * is optional, as it will be executed upon closing this manager.
	 */
	ChangesetPojoWorker getWorker();

	/**
	 * @return A stream worker for this manager.
	 */
	StreamPojoWorker getStreamWorker();

}
