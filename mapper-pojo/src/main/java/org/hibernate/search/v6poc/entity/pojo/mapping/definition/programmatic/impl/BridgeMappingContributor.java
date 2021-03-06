/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.definition.programmatic.impl;

import org.hibernate.search.v6poc.bridge.mapping.BridgeDefinition;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoNodeMappingCollector;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoNodeMetadataContributor;
import org.hibernate.search.v6poc.entity.pojo.mapping.building.impl.PojoNodeModelCollector;


/**
 * @author Yoann Rodiere
 */
public class BridgeMappingContributor
		implements PojoNodeMetadataContributor<PojoNodeModelCollector, PojoNodeMappingCollector> {

	private final BridgeDefinition<?> definition;

	public BridgeMappingContributor(BridgeDefinition<?> definition) {
		this.definition = definition;
	}

	@Override
	public void contributeModel(PojoNodeModelCollector collector) {
		// Nothing to do
	}

	@Override
	public void contributeMapping(PojoNodeMappingCollector collector) {
		collector.bridge( definition );
	}

}
