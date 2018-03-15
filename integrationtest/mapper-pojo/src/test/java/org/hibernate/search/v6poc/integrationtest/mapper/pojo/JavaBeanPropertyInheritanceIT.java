/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.integrationtest.mapper.pojo;

import org.hibernate.search.v6poc.engine.SearchMappingRepository;
import org.hibernate.search.v6poc.engine.SearchMappingRepositoryBuilder;
import org.hibernate.search.v6poc.entity.javabean.JavaBeanMapping;
import org.hibernate.search.v6poc.entity.javabean.JavaBeanMappingContributor;
import org.hibernate.search.v6poc.entity.pojo.mapping.PojoSearchManager;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.annotation.Field;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.v6poc.integrationtest.util.common.rule.BackendMock;
import org.hibernate.search.v6poc.integrationtest.util.common.rule.StaticCounters;
import org.hibernate.search.v6poc.integrationtest.util.common.stub.backend.index.impl.StubBackendFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Yoann Rodiere
 */
public class JavaBeanPropertyInheritanceIT {

	@Rule
	public BackendMock backendMock = new BackendMock( "stubBackend" );

	@Rule
	public StaticCounters counters = new StaticCounters();

	private SearchMappingRepository mappingRepository;
	private JavaBeanMapping mapping;

	@Before
	public void setup() {
		SearchMappingRepositoryBuilder mappingRepositoryBuilder = SearchMappingRepository.builder()
				.setProperty( "backend.stubBackend.type", StubBackendFactory.class.getName() )
				.setProperty( "index.default.backend", "stubBackend" );

		JavaBeanMappingContributor contributor = new JavaBeanMappingContributor( mappingRepositoryBuilder );

		contributor.annotationMapping().add( IndexedEntity.class );

		backendMock.expectSchema( IndexedEntity.INDEX, b -> b
				.field( "parentDeclaredProperty", String.class )
				.field( "childDeclaredProperty", String.class )
				.objectField( "embedded", b2 -> b2
						.field( "parentDeclaredProperty", String.class )
						// If property inheritance is not handled correctly, this field will be missing
						.field( "childDeclaredProperty", String.class )
				)
		);

		mappingRepository = mappingRepositoryBuilder.build();
		mapping = contributor.getResult();
		backendMock.verifyExpectationsMet();
	}

	@After
	public void cleanup() {
		if ( mappingRepository != null ) {
			mappingRepository.close();
		}
	}

	@Test
	public void index() {
		try ( PojoSearchManager manager = mapping.createSearchManager() ) {
			IndexedEntity entity1 = new IndexedEntity();
			entity1.setId( 1 );
			entity1.setParentDeclaredProperty( "parent-declared-1" );
			entity1.setChildDeclaredProperty( "child-declared-1" );
			IndexedEntity entity2 = new IndexedEntity();
			entity2.setId( 2 );
			entity1.setParentDeclaredProperty( "parent-declared-2" );
			entity1.setChildDeclaredProperty( "child-declared-2" );

			entity1.setEmbedded( entity2 );

			manager.getMainWorker().add( entity1 );

			backendMock.expectWorks( IndexedEntity.INDEX )
					.add( "1", b -> b
							.field( "parentDeclaredProperty", entity1.getParentDeclaredProperty() )
							.field( "childDeclaredProperty", entity1.getChildDeclaredProperty() )
							.objectField( "embedded", b2 -> b2
									.field( "parentDeclaredProperty", entity2.getParentDeclaredProperty() )
									.field( "childDeclaredProperty", entity2.getChildDeclaredProperty() )
							)
					)
					.preparedThenExecuted();
		}
	}

	public abstract static class ParentIndexedEntity {

		private String parentDeclaredProperty;

		@Field
		public String getParentDeclaredProperty() {
			return parentDeclaredProperty;
		}

		public void setParentDeclaredProperty(String parentDeclaredProperty) {
			this.parentDeclaredProperty = parentDeclaredProperty;
		}

		@IndexedEmbedded(maxDepth = 1)
		public abstract ParentIndexedEntity getEmbedded();
	}

	@Indexed(index = IndexedEntity.INDEX)
	public static final class IndexedEntity extends ParentIndexedEntity {

		static final String INDEX = "IndexedEntity";

		private Integer id;

		private String childDeclaredProperty;

		private IndexedEntity embedded;

		@DocumentId
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@Field
		public String getChildDeclaredProperty() {
			return childDeclaredProperty;
		}

		public void setChildDeclaredProperty(String childDeclaredProperty) {
			this.childDeclaredProperty = childDeclaredProperty;
		}

		@Override
		public IndexedEntity getEmbedded() {
			return embedded;
		}

		public void setEmbedded(IndexedEntity embedded) {
			this.embedded = embedded;
		}
	}


}