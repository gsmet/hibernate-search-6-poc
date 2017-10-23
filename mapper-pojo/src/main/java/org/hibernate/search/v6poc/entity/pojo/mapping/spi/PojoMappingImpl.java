/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.spi;

import org.hibernate.search.v6poc.entity.mapping.spi.MappingImplementor;
import org.hibernate.search.v6poc.entity.pojo.mapping.PojoMapping;

public class PojoMappingImpl implements PojoMapping, MappingImplementor {

	private final PojoMappingDelegate delegate;

	public PojoMappingImpl(PojoMappingDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void close() {
		delegate.close();
	}

	protected final PojoMappingDelegate getDelegate() {
		return delegate;
	}

}
