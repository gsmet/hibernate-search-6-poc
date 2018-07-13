/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.spi;

import org.hibernate.search.v6poc.backend.Backend;
import org.hibernate.search.v6poc.backend.document.DocumentElement;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;

/**
 * @author Yoann Rodiere
 */
public interface BackendImplementor<D extends DocumentElement> extends AutoCloseable {

	/**
	 * @return The object that should be exposed as API to users.
	 */
	Backend toAPI();

	/**
	 * @param indexName The name of the index from the point of view of Hibernate Search.
	 * A slightly different name may be used by the backend internally,
	 * but {@code indexName} is the one that will appear everywhere the index is mentioned to the user.
	 * @param multiTenancyEnabled {@code true} if multi-tenancy is enabled for this index, {@code false} otherwise.
	 * @param context The build context
	 * @param propertySource A configuration property source, appropriately masked so that the backend
	 * doesn't need to care about Hibernate Search prefixes (hibernate.search.*, etc.). All the properties
	 * can be accessed at the root.
	 * <strong>CAUTION:</strong> the property key {@code backend} is reserved for use by the engine.
	 * @return A builder for index managers targeting this backend.
	 */
	IndexManagerBuilder<D> createIndexManagerBuilder(String indexName, boolean multiTenancyEnabled, BackendBuildContext context,
			ConfigurationPropertySource propertySource);

	@Override
	void close();

}
