/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.processing.impl;

import org.hibernate.search.v6poc.backend.document.spi.DocumentState;
import org.hibernate.search.v6poc.bridge.spi.Bridge;
import org.hibernate.search.v6poc.entity.model.spi.Indexable;
import org.hibernate.search.v6poc.entity.processing.spi.ValueProcessor;


/**
 * @author Yoann Rodiere
 */
public class BridgeValueProcessor implements ValueProcessor {

	private final Bridge<?> bridge;

	public BridgeValueProcessor(Bridge<?> bridge) {
		this.bridge = bridge;
	}

	@Override
	public void process(Indexable source, DocumentState target) {
		bridge.toDocument( source, target );
	}

	@Override
	public void close() {
		bridge.close();
	}

}
