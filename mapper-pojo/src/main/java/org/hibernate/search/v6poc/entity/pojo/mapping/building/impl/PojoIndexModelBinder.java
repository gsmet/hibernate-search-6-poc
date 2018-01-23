/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.building.impl;

import org.hibernate.search.v6poc.backend.document.model.IndexSchemaElement;
import org.hibernate.search.v6poc.entity.mapping.building.spi.FieldModelContributor;
import org.hibernate.search.v6poc.entity.mapping.building.spi.IndexModelBindingContext;
import org.hibernate.search.v6poc.entity.pojo.bridge.mapping.BridgeBuilder;
import org.hibernate.search.v6poc.entity.pojo.bridge.Bridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.FunctionBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.IdentifierBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.RoutingKeyBridge;
import org.hibernate.search.v6poc.entity.pojo.model.PojoModelElement;
import org.hibernate.search.v6poc.entity.pojo.processing.impl.ValueProcessor;

/**
 * Provides the ability to contribute the entity model to the index model
 * by creating bridges and
 * {@link Bridge#bind(IndexSchemaElement, PojoModelElement) binding}
 * them.
 * <p>
 * Incidentally, this will also generate the index model,
 * due to bridges contributing to the index model as we bind them.
 *
 * @author Yoann Rodiere
 */
public interface PojoIndexModelBinder {

	<T> IdentifierBridge<T> createIdentifierBridge(Class<T> sourceType,
			BridgeBuilder<? extends IdentifierBridge<?>> bridgeBuilder);

	RoutingKeyBridge addRoutingKeyBridge(IndexModelBindingContext bindingContext,
			PojoModelElement pojoModelElement, BridgeBuilder<? extends RoutingKeyBridge> bridgeBuilder);

	ValueProcessor addBridge(IndexModelBindingContext bindingContext,
			PojoModelElement pojoModelElement, BridgeBuilder<? extends Bridge> bridgeBuilder);

	ValueProcessor addFunctionBridge(IndexModelBindingContext bindingContext,
			PojoModelElement pojoModelElement, Class<?> sourceType,
			BridgeBuilder<? extends FunctionBridge<?, ?>> bridgeBuilder,
			String fieldName, FieldModelContributor contributor);

}
