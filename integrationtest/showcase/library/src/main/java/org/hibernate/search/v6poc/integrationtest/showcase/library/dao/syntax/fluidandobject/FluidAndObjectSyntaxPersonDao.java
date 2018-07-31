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
import org.hibernate.search.v6poc.entity.orm.jpa.FullTextSearchTarget;
import org.hibernate.search.v6poc.integrationtest.showcase.library.dao.PersonDao;
import org.hibernate.search.v6poc.integrationtest.showcase.library.model.Person;

class FluidAndObjectSyntaxPersonDao extends PersonDao {

	FluidAndObjectSyntaxPersonDao(EntityManager entityManager) {
		super( entityManager );
	}

	@Override
	public List<Person> search(String terms, int offset, int limit) {
		if ( terms == null || terms.isEmpty() ) {
			return Collections.emptyList();
		}

		FullTextSearchTarget<Person> target = entityManager.search( Person.class );

		FullTextQuery<Person> query = target.query()
				.asEntities()
				.predicate().match().onFields( "firstName", "lastName" ).matching( terms ).end()
				.sort().byField( "lastName_sort" )
						.then().byField( "firstName_sort" )
						.end()
				.build();

		query.setFirstResult( offset );
		query.setMaxResults( limit );

		return query.getResultList();
	}

	@Override
	public List<Person> listTopBorrowers(String borrowalsCountField, int offset, int limit) {
		FullTextSearchTarget<Person> target = entityManager.search( Person.class );

		FullTextQuery<Person> query = target.query()
				.asEntities()
				.predicate(
						target.predicate().matchAll().end()
				)
				.sort(
						target.sort().by( target.sort().byField( borrowalsCountField ).desc().end() ).end()
				)
				.build();

		query.setFirstResult( offset );
		query.setMaxResults( limit );

		return query.getResultList();
	}
}
