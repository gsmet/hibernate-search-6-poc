/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.bridge.spi;

import java.lang.annotation.Annotation;

import org.hibernate.search.v6poc.backend.document.model.spi.IndexModelCollector;
import org.hibernate.search.v6poc.backend.document.spi.DocumentState;
import org.hibernate.search.v6poc.engine.spi.BuildContext;
import org.hibernate.search.v6poc.entity.model.spi.Indexable;
import org.hibernate.search.v6poc.entity.model.spi.IndexableModel;

/**
 * @author Yoann Rodiere
 */
public interface Bridge<A extends Annotation> extends AutoCloseable {

	/* Solves HSEARCH-1306 */
	void initialize(BuildContext buildContext, A parameters);

	void bind(IndexableModel indexableModel, IndexModelCollector indexModelCollector);

	void toDocument(Indexable source, DocumentState target);

	@Override
	default void close() {
	}

}
