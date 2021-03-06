/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.javabean.mapping;

import org.hibernate.search.v6poc.engine.SearchManagerBuilder;
import org.hibernate.search.v6poc.entity.javabean.mapping.impl.JavaBeanMapperImplementor;
import org.hibernate.search.v6poc.entity.mapping.MappingType;
import org.hibernate.search.v6poc.entity.pojo.mapping.PojoSearchManager;


/**
 * @author Yoann Rodiere
 */
public final class JavaBeanMappingType {

	public static MappingType<PojoSearchManager, SearchManagerBuilder<PojoSearchManager>> get() {
		return JavaBeanMapperImplementor.get();
	}

}
