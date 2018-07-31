/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.integrationtest.showcase.library.dao.syntax.fluidandobject;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;

import org.hibernate.search.v6poc.entity.orm.jpa.FullTextQuery;
import org.hibernate.search.v6poc.integrationtest.showcase.library.dao.LibraryDao;
import org.hibernate.search.v6poc.integrationtest.showcase.library.model.Library;

class FluidAndObjectSyntaxLibraryDao extends LibraryDao {

	FluidAndObjectSyntaxLibraryDao(EntityManager entityManager) {
		super( entityManager );
	}

	@Override
	public List<Library> search(String terms, int offset, int limit) {
		if ( terms == null || terms.isEmpty() ) {
			return Collections.emptyList();
		}
		FullTextQuery<Library> query = entityManager.search( Library.class ).query()
				.asEntities()
				.predicate().match().onField( "name" ).matching( terms ).end()
				.sort().byField( "collectionSize" ).desc()
						.then().byField( "name_sort" )
						.end()
				.build();

		query.setFirstResult( offset );
		query.setMaxResults( limit );

		return query.getResultList();
	}

}
