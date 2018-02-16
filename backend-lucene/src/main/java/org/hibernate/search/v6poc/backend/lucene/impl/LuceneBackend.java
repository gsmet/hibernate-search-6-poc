/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.impl;

import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.orchestration.impl.LuceneWorkOrchestrator;
import org.hibernate.search.v6poc.backend.lucene.work.impl.LuceneWorkFactory;
import org.hibernate.search.v6poc.backend.spi.Backend;

/**
 * @author Guillaume Smet
 */
public interface LuceneBackend extends Backend<LuceneDocumentBuilder> {

	LuceneWorkFactory getWorkFactory();

	LuceneWorkOrchestrator createChangesetOrchestrator();

	LuceneWorkOrchestrator getStreamOrchestrator();

	LuceneWorkOrchestrator getQueryOrchestrator();
}
