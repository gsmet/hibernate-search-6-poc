/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.processing.impl;

import java.util.function.Supplier;

import org.hibernate.search.v6poc.entity.pojo.model.impl.PojoIndexable;
import org.hibernate.search.v6poc.entity.processing.RoutingKeyBridge;

public class RoutingKeyBridgeProvider<E> implements RoutingKeyProvider<E> {

	private final RoutingKeyBridge bridge;

	public RoutingKeyBridgeProvider(RoutingKeyBridge bridge) {
		this.bridge = bridge;
	}

	@Override
	public String toRoutingKey(Object identifier, Supplier<E> entitySupplier) {
		return bridge.toRoutingKey( identifier, new PojoIndexable( entitySupplier.get() ) );
	}
}