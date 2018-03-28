/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.mapping.spi;

/**
 * Interface used by the engine to manipulate mappings
 * <p>
 * Publicly exposed mapping interfaces do not have to extend this interface;
 * only the implementations have to implement it.
 *
 * @param <M> The API type for this implementor.
 */
public interface MappingImplementor<M> extends AutoCloseable {

	M toAPI();

	@Override
	void close();

}
