/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.definition.programmatic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.v6poc.bridge.mapping.BridgeDefinition;
import org.hibernate.search.v6poc.entity.mapping.building.spi.MetadataContributor;
import org.hibernate.search.v6poc.entity.mapping.building.spi.TypeMetadataCollector;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoNodeMetadataContributor;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoTypeNodeMappingCollector;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoTypeNodeMetadataContributor;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoTypeNodeModelCollector;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.spi.PojoMapperImplementor;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.programmatic.PropertyMappingContext;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.programmatic.TypeMappingContext;
import org.hibernate.search.v6poc.entity.pojo.model.impl.PojoIndexedTypeIdentifier;

/**
 * @author Yoann Rodiere
 */
public class TypeMappingContextImpl implements TypeMappingContext, MetadataContributor, PojoTypeNodeMetadataContributor {

	private final PojoMapperImplementor mappingType;
	private final Class<?> type;

	private String indexName;
	private final List<PojoNodeMetadataContributor<? super PojoTypeNodeModelCollector, ? super PojoTypeNodeMappingCollector>>
			children = new ArrayList<>();

	public TypeMappingContextImpl(PojoMapperImplementor mappingType, Class<?> type) {
		this.mappingType = mappingType;
		this.type = type;
	}

	@Override
	public void contribute(TypeMetadataCollector collector) {
		collector.collect( mappingType, new PojoIndexedTypeIdentifier( type ), indexName, this );
	}

	@Override
	public void contributeModel(PojoTypeNodeModelCollector collector) {
		children.stream().forEach( c -> c.contributeModel( collector ) );
	}

	@Override
	public void contributeMapping(PojoTypeNodeMappingCollector collector) {
		children.stream().forEach( c -> c.contributeMapping( collector ) );
	}

	@Override
	public TypeMappingContext indexed() {
		return indexed( type.getName() );
	}

	@Override
	public TypeMappingContext indexed(String indexName) {
		this.indexName = indexName;
		return this;
	}

	@Override
	public TypeMappingContext bridge(BridgeDefinition<?> definition) {
		children.add( new BridgeMappingContributor( definition ) );
		return this;
	}

	@Override
	public PropertyMappingContext property(String propertyName) {
		PropertyMappingContextImpl child = new PropertyMappingContextImpl( this, propertyName );
		children.add( child );
		return child;
	}

}
