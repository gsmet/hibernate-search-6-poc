/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.model.additionalmetadata.impl;

import java.util.Optional;

import org.hibernate.search.v6poc.entity.pojo.model.path.PojoModelPathValueNode;

public class PojoValueAdditionalMetadata {

	public static final PojoValueAdditionalMetadata EMPTY = new PojoValueAdditionalMetadata( null, false );

	private final PojoModelPathValueNode inverseSidePath;
	private final boolean associationEmbedded;

	public PojoValueAdditionalMetadata(PojoModelPathValueNode inverseSidePath, boolean associationEmbedded) {
		this.inverseSidePath = inverseSidePath;
		this.associationEmbedded = associationEmbedded;
	}

	public Optional<PojoModelPathValueNode> getInverseSidePath() {
		return Optional.ofNullable( inverseSidePath );
	}

	public boolean isAssociationEmbedded() {
		return associationEmbedded;
	}

}