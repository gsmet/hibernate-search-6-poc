/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.model.impl;

import org.hibernate.search.v6poc.entity.pojo.model.PojoElement;
import org.hibernate.search.v6poc.entity.pojo.model.PojoModelElementAccessor;
import org.hibernate.search.v6poc.entity.pojo.model.spi.PropertyHandle;

class PojoModelPropertyElementAccessor<T> implements PojoModelElementAccessor<T> {

	private final PojoModelElementAccessor<?> parent;
	private final PropertyHandle handle;

	PojoModelPropertyElementAccessor(PojoModelElementAccessor<?> parent, PropertyHandle handle) {
		this.parent = parent;
		this.handle = handle;
	}

	@Override
	public T read(PojoElement bridgedElement) {
		Object parentValue = parent.read( bridgedElement );
		if ( parentValue != null ) {
			return (T) handle.get( parentValue );
		}
		else {
			return null;
		}
	}

}