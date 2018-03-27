/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.javabean.mapping.impl;

import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;
import org.hibernate.search.v6poc.entity.javabean.model.impl.JavaBeanBootstrapIntrospector;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.spi.PojoMapperFactory;
import org.hibernate.search.v6poc.entity.pojo.mapping.spi.PojoMappingDelegate;


/**
 * @author Yoann Rodiere
 */
public final class JavaBeanMapperFactory extends PojoMapperFactory<JavaBeanMappingImpl> {

	public JavaBeanMapperFactory(JavaBeanBootstrapIntrospector introspector) {
		super( introspector, false );
	}

	@Override
	protected JavaBeanMappingImpl createMapping(ConfigurationPropertySource propertySource,
			PojoMappingDelegate mappingDelegate) {
		return new JavaBeanMappingImpl( mappingDelegate );
	}
}