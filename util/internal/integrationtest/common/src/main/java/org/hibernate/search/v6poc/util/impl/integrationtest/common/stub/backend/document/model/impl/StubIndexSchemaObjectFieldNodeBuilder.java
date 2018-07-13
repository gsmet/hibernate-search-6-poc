/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.util.impl.integrationtest.common.stub.backend.document.model.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.search.v6poc.backend.document.IndexObjectFieldAccessor;
import org.hibernate.search.v6poc.backend.document.model.dsl.spi.IndexSchemaObjectFieldNodeBuilder;
import org.hibernate.search.v6poc.logging.spi.FailureContextElement;
import org.hibernate.search.v6poc.logging.spi.FailureContexts;
import org.hibernate.search.v6poc.util.impl.integrationtest.common.stub.backend.document.model.StubIndexSchemaNode;

class StubIndexSchemaObjectFieldNodeBuilder extends StubIndexSchemaObjectNodeBuilder
		implements IndexSchemaObjectFieldNodeBuilder {

	private StubIndexSchemaObjectNodeBuilder parent;
	private final boolean included;
	private IndexObjectFieldAccessor accessor;

	StubIndexSchemaObjectFieldNodeBuilder(StubIndexSchemaObjectNodeBuilder parent,
			StubIndexSchemaNode.Builder builder, boolean included) {
		super( builder );
		this.parent = parent;
		this.included = included;
	}

	@Override
	public List<FailureContextElement> getFailureContext() {
		return Arrays.asList(
				getRootNodeBuilder().getIndexFailureContextElement(),
				FailureContexts.fromIndexFieldAbsolutePath( builder.getAbsolutePath() )
		);
	}

	@Override
	public IndexObjectFieldAccessor getAccessor() {
		if ( accessor == null ) {
			if ( included ) {
				accessor = new StubIncludedIndexObjectFieldAccessor(
						builder.getAbsolutePath(), builder.getRelativeName()
				);
			}
			else {
				accessor = new StubExcludedIndexObjectFieldAccessor(
						builder.getAbsolutePath(), builder.getRelativeName()
				);
			}
		}
		return accessor;
	}

	@Override
	StubIndexSchemaRootNodeBuilder getRootNodeBuilder() {
		return parent.getRootNodeBuilder();
	}
}
