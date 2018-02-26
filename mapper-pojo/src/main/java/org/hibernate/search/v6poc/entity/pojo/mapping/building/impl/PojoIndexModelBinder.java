/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.building.impl;

import java.util.Optional;

import org.hibernate.search.v6poc.backend.document.model.IndexSchemaElement;
import org.hibernate.search.v6poc.backend.document.model.IndexSchemaFieldContext;
import org.hibernate.search.v6poc.entity.mapping.building.spi.FieldModelContributor;
import org.hibernate.search.v6poc.entity.mapping.building.spi.IndexModelBindingContext;
import org.hibernate.search.v6poc.entity.model.SearchModel;
import org.hibernate.search.v6poc.entity.pojo.bridge.PropertyBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.FunctionBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.IdentifierBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.RoutingKeyBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.TypeBridge;
import org.hibernate.search.v6poc.entity.pojo.bridge.mapping.BridgeBuilder;
import org.hibernate.search.v6poc.entity.pojo.extractor.impl.BoundContainerValueExtractor;
import org.hibernate.search.v6poc.entity.pojo.extractor.impl.ContainerValueExtractor;
import org.hibernate.search.v6poc.entity.pojo.model.PojoModelElement;
import org.hibernate.search.v6poc.entity.pojo.model.PojoModelProperty;
import org.hibernate.search.v6poc.entity.pojo.model.PojoModelType;
import org.hibernate.search.v6poc.entity.pojo.model.spi.PojoGenericTypeModel;
import org.hibernate.search.v6poc.entity.pojo.model.spi.PojoTypeModel;
import org.hibernate.search.v6poc.entity.pojo.processing.impl.FunctionBridgeProcessor;

/**
 * Binds a mapping to a given entity model and index model
 * by creating the appropriate {@link ContainerValueExtractor extractors} and bridges.
 * <p>
 * Also binds the bridges where appropriate:
 * {@link TypeBridge#bind(IndexSchemaElement, PojoModelType, SearchModel)},
 * {@link PropertyBridge#bind(IndexSchemaElement, PojoModelProperty, SearchModel)},
 * {@link FunctionBridge#bind(IndexSchemaFieldContext)}.
 * <p>
 * Incidentally, this will also generate the index model,
 * due to bridges contributing to the index model as we bind them.
 *
 * @author Yoann Rodiere
 */
public interface PojoIndexModelBinder {

	<T> Optional<BoundContainerValueExtractor<? super T, ?>> createDefaultExtractor(
			PojoGenericTypeModel<T> pojoGenericTypeModel);

	<T> IdentifierBridge<T> createIdentifierBridge(PojoModelElement pojoModelElement, PojoTypeModel<T> typeModel,
			BridgeBuilder<? extends IdentifierBridge<?>> bridgeBuilder);

	RoutingKeyBridge addRoutingKeyBridge(IndexModelBindingContext bindingContext,
			PojoModelElement pojoModelElement, BridgeBuilder<? extends RoutingKeyBridge> bridgeBuilder);

	Optional<TypeBridge> addTypeBridge(IndexModelBindingContext bindingContext,
			PojoModelType pojoModelType, BridgeBuilder<? extends TypeBridge> bridgeBuilder);

	Optional<PropertyBridge> addPropertyBridge(IndexModelBindingContext bindingContext,
			PojoModelProperty pojoModelProperty, BridgeBuilder<? extends PropertyBridge> bridgeBuilder);

	<T> Optional<FunctionBridgeProcessor<T, ?>> addFunctionBridge(IndexModelBindingContext bindingContext,
			PojoTypeModel<T> typeModel, BridgeBuilder<? extends FunctionBridge<?, ?>> bridgeBuilder,
			String fieldName, FieldModelContributor contributor);

}
