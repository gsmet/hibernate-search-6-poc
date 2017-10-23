/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.orm.mapping.impl;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.search.v6poc.entity.orm.mapping.HibernateOrmSearchManager;
import org.hibernate.search.v6poc.entity.orm.mapping.HibernateOrmSearchManagerBuilder;
import org.hibernate.search.v6poc.entity.orm.model.impl.HibernateOrmProxyIntrospector;
import org.hibernate.search.v6poc.entity.pojo.mapping.spi.PojoMappingDelegate;
import org.hibernate.search.v6poc.entity.pojo.mapping.spi.PojoSearchManagerImpl;
import org.hibernate.search.v6poc.entity.pojo.model.spi.PojoProxyIntrospector;

class HibernateOrmSearchManagerImpl extends PojoSearchManagerImpl
		implements HibernateOrmSearchManager {
	private final SessionImplementor sessionImplementor;

	private HibernateOrmSearchManagerImpl(Builder builder) {
		super( builder );
		this.sessionImplementor = builder.sessionImplementor;
	}

	static class Builder extends PojoSearchManagerImpl.Builder<HibernateOrmSearchManager>
			implements HibernateOrmSearchManagerBuilder {
		private final SessionImplementor sessionImplementor;

		public Builder(PojoMappingDelegate mappingDelegate, SessionImplementor sessionImplementor) {
			super( mappingDelegate );
			this.sessionImplementor = sessionImplementor;
		}

		@Override
		protected String getTenantId() {
			return sessionImplementor.getTenantIdentifier();
		}

		@Override
		protected PojoProxyIntrospector getProxyIntrospector() {
			return new HibernateOrmProxyIntrospector( sessionImplementor );
		}

		@Override
		public HibernateOrmSearchManager build() {
			return new HibernateOrmSearchManagerImpl( this );
		}
	}
}
