/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc;

import java.util.List;
import java.util.Map;

import org.hibernate.search.v6poc.backend.elasticsearch.client.impl.StubElasticsearchClient;
import org.hibernate.search.v6poc.backend.elasticsearch.client.impl.StubElasticsearchClient.Request;
import org.hibernate.search.v6poc.backend.elasticsearch.impl.ElasticsearchBackendFactory;
import org.hibernate.search.v6poc.engine.SearchMappingRepository;
import org.hibernate.search.v6poc.engine.SearchMappingRepositoryBuilder;
import org.hibernate.search.v6poc.entity.javabean.JavaBeanMapping;
import org.hibernate.search.v6poc.entity.javabean.JavaBeanMappingContributor;
import org.hibernate.search.v6poc.entity.model.spi.Indexable;
import org.hibernate.search.v6poc.entity.model.spi.IndexableModel;
import org.hibernate.search.v6poc.entity.model.spi.IndexableReference;
import org.hibernate.search.v6poc.entity.pojo.mapping.PojoSearchManager;
import org.hibernate.search.v6poc.entity.pojo.mapping.definition.programmatic.MappingDefinition;
import org.hibernate.search.v6poc.entity.processing.RoutingKeyBridge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.json.JSONException;

import static org.hibernate.search.v6poc.util.StubAssert.assertRequest;

/**
 * @author Yoann Rodiere
 */
public class JavaBeanElasticsearchRoutingIT {

	private SearchMappingRepository mappingRepository;

	private JavaBeanMapping mapping;

	private static final String HOST_1 = "http://es1.mycompany.com:9200/";

	@Before
	public void setup() throws JSONException {
		SearchMappingRepositoryBuilder mappingRepositoryBuilder = SearchMappingRepository.builder()
				.setProperty( "backend.elasticsearchBackend_1.type", ElasticsearchBackendFactory.class.getName() )
				.setProperty( "backend.elasticsearchBackend_1.host", HOST_1 )
				.setProperty( "index.default.backend", "elasticsearchBackend_1" );

		JavaBeanMappingContributor contributor = new JavaBeanMappingContributor( mappingRepositoryBuilder );

		MappingDefinition mappingDefinition = contributor.programmaticMapping();
		mappingDefinition.type( IndexedEntity.class )
				.indexed( IndexedEntity.INDEX )
				.routingKeyBridge( MyRoutingKeyBridge.class )
				.property( "id" )
						.documentId()
				.property( "value" ).field();

		mappingRepository = mappingRepositoryBuilder.build();
		mapping = contributor.getResult();

		Map<String, List<Request>> requests = StubElasticsearchClient.drainRequestsByIndex();

		assertRequest( requests, IndexedEntity.INDEX, 0, HOST_1, "createIndex", null,
				"{"
					+ "'mapping': {"
						+ "'_routing': {"
							+ "'required': true"
						+ "},"
						+ "'properties': {"
							+ "'value': {"
								+ "'type': 'keyword'"
							+ "}"
						+ "}"
					+ "}"
				+ "}" );
	}

	@After
	public void cleanup() {
		StubElasticsearchClient.drainRequestsByIndex();
		if ( mappingRepository != null ) {
			mappingRepository.close();
		}
	}

	@Test
	public void index() throws JSONException {
		try (PojoSearchManager manager = mapping.createSearchManager()) {
			IndexedEntity entity1 = new IndexedEntity();
			entity1.setId( 1 );
			entity1.setCategory( EntityCategory.CATEGORY_2 );
			entity1.setValue( "val1" );

			manager.getMainWorker().add( entity1 );
		}

		Map<String, List<Request>> requests = StubElasticsearchClient.drainRequestsByIndex();
		assertRequest( requests, IndexedEntity.INDEX, 0, HOST_1, "add", "1",
				c -> {
					c.accept( "_routing", "category_2" );
				},
				"{"
					+ "'value': 'val1'"
				+ "}" );
	}

	// TODO implement and test routing predicates when searching
	// TODO also implement filters and allow them to use routing predicates

	public enum EntityCategory {
		CATEGORY_1,
		CATEGORY_2;
	}

	public static final class IndexedEntity {

		public static final String INDEX = "IndexedEntity";

		private Integer id;

		private EntityCategory category;

		private String value;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public EntityCategory getCategory() {
			return category;
		}

		public void setCategory(EntityCategory category) {
			this.category = category;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public static final class MyRoutingKeyBridge implements RoutingKeyBridge {

		private IndexableReference<EntityCategory> categoryReference;

		@Override
		public void bind(IndexableModel indexableModel) {
			categoryReference = indexableModel.property( "category" ).asReference( EntityCategory.class );
		}

		@Override
		public String toRoutingKey(Object entityIdentifier, Indexable source) {
			EntityCategory category = source.get( categoryReference );
			switch ( category ) {
				case CATEGORY_1:
					return "category_1";
				case CATEGORY_2:
					return "category_2";
				default:
					throw new RuntimeException( "Unknown category: " + category );
			}
		}
	}

}