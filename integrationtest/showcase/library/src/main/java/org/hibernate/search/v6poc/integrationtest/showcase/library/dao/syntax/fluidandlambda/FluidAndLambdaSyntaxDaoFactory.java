/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.integrationtest.showcase.library.dao.syntax.fluidandlambda;

import javax.persistence.EntityManager;

import org.hibernate.search.v6poc.integrationtest.showcase.library.dao.DaoFactory;
import org.hibernate.search.v6poc.integrationtest.showcase.library.dao.DocumentDao;
import org.hibernate.search.v6poc.integrationtest.showcase.library.dao.LibraryDao;

public class FluidAndLambdaSyntaxDaoFactory implements DaoFactory {
	@Override
	public DocumentDao createDocumentDao(EntityManager entityManager) {
		return new FluidAndLambdaSyntaxDocumentDao( entityManager );
	}

	@Override
	public LibraryDao createLibraryDao(EntityManager entityManager) {
		return new FluidAndLambdaSyntaxLibraryDao( entityManager );
	}
}
