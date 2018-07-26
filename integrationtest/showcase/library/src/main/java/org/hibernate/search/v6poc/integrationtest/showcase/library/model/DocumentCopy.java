/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.integrationtest.showcase.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.hibernate.search.v6poc.entity.pojo.mapping.definition.annotation.IndexedEmbedded;

/**
 * A concrete copy of a document, be it physical or dematerialized, that can be borrowed.
 *
 * @param <D> The type of document.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DocumentCopy<D extends Document<?>> extends AbstractEntity<Integer> {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne(targetEntity = Document.class)
	@IndexedEmbedded
	private D document;

	@ManyToOne
	@IndexedEmbedded(maxDepth = 1)
	// TODO facet
	private Library library;

	@Override
	public Integer getId() {
		return id;
	}

	public D getDocument() {
		return document;
	}

	public void setDocument(D document) {
		this.document = document;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	@Override
	protected String getDescriptionForToString() {
		return "document=" + document + ",library=" + library;
	}
}
