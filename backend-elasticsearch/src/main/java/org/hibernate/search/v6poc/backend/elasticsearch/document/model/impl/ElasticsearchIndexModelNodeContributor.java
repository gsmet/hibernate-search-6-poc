/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.document.model.impl;

/**
 * @author Yoann Rodiere
 */
public interface ElasticsearchIndexModelNodeContributor<T> {

	void contribute(T mapping, ElasticsearchFieldModelCollector collector);

}
