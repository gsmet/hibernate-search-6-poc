/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.building.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.search.v6poc.entity.pojo.model.additionalmetadata.building.spi.PojoAdditionalMetadataCollectorPropertyNode;

public final class DelegatingPojoPropertyMetadataContributor implements PojoPropertyMetadataContributor {

	private List<PojoPropertyMetadataContributor> children;

	@Override
	public void contributeModel(PojoAdditionalMetadataCollectorPropertyNode collector) {
		if ( children != null ) {
			for ( PojoPropertyMetadataContributor child : children ) {
				child.contributeModel( collector );
			}
		}
	}

	@Override
	public void contributeMapping(PojoMappingCollectorPropertyNode collector) {
		if ( children != null ) {
			for ( PojoPropertyMetadataContributor child : children ) {
				child.contributeMapping( collector );
			}
		}
	}

	public DelegatingPojoPropertyMetadataContributor addAll(Collection<? extends PojoPropertyMetadataContributor> children) {
		initChildren();
		this.children.addAll( children );
		return this;
	}

	public DelegatingPojoPropertyMetadataContributor add(PojoPropertyMetadataContributor child) {
		initChildren();
		this.children.add( child );
		return this;
	}

	private void initChildren() {
		if ( this.children == null ) {
			this.children = new ArrayList<>();
		}
	}
}
