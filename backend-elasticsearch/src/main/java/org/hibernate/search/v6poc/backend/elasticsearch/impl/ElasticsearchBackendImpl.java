/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.elasticsearch.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.hibernate.search.v6poc.backend.Backend;
import org.hibernate.search.v6poc.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.v6poc.backend.elasticsearch.client.impl.ElasticsearchClient;
import org.hibernate.search.v6poc.backend.elasticsearch.document.impl.ElasticsearchDocumentObjectBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.document.model.dsl.impl.ElasticsearchIndexSchemaRootNodeBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.index.impl.ElasticsearchIndexManagerBuilder;
import org.hibernate.search.v6poc.backend.elasticsearch.index.impl.IndexingBackendContext;
import org.hibernate.search.v6poc.backend.elasticsearch.multitenancy.impl.MultiTenancyStrategy;
import org.hibernate.search.v6poc.backend.elasticsearch.search.query.impl.SearchBackendContext;
import org.hibernate.search.v6poc.backend.elasticsearch.logging.impl.Log;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.ElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.orchestration.impl.StubElasticsearchWorkOrchestrator;
import org.hibernate.search.v6poc.backend.elasticsearch.work.impl.ElasticsearchWorkFactory;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.backend.spi.BackendImplementor;
import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;
import org.hibernate.search.v6poc.backend.spi.BackendBuildContext;
import org.hibernate.search.v6poc.logging.spi.FailureContext;
import org.hibernate.search.v6poc.logging.spi.FailureContexts;
import org.hibernate.search.v6poc.util.SearchException;
import org.hibernate.search.v6poc.util.impl.common.Closer;
import org.hibernate.search.v6poc.util.impl.common.LoggerFactory;


/**
 * @author Yoann Rodiere
 */
class ElasticsearchBackendImpl implements BackendImplementor<ElasticsearchDocumentObjectBuilder>,
		ElasticsearchBackend {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final ElasticsearchClient client;

	private final String name;

	private final MultiTenancyStrategy multiTenancyStrategy;

	private final ElasticsearchWorkOrchestrator streamOrchestrator;
	private final ElasticsearchWorkOrchestrator queryOrchestrator;

	private final Map<String, String> hibernateSearchIndexNamesByElasticsearchIndexNames = new ConcurrentHashMap<>();

	private final FailureContext failureContext;
	private final IndexingBackendContext indexingContext;
	private final SearchBackendContext searchContext;

	ElasticsearchBackendImpl(ElasticsearchClient client, String name, ElasticsearchWorkFactory workFactory,
			MultiTenancyStrategy multiTenancyStrategy) {
		this.client = client;
		this.name = name;
		this.multiTenancyStrategy = multiTenancyStrategy;
		this.streamOrchestrator = new StubElasticsearchWorkOrchestrator( client );
		this.queryOrchestrator = new StubElasticsearchWorkOrchestrator( client );

		this.failureContext = FailureContexts.fromBackendName( name );
		this.indexingContext = new IndexingBackendContext(
				failureContext, client, workFactory, multiTenancyStrategy, streamOrchestrator
		);
		this.searchContext = new SearchBackendContext(
				failureContext, workFactory,
				new Function<String, String>() {
					@Override
					public String apply(String elasticsearchIndexName) {
						String result = hibernateSearchIndexNamesByElasticsearchIndexNames.get( elasticsearchIndexName );
						if ( result == null ) {
							throw log.elasticsearchResponseUnknownIndexName( elasticsearchIndexName, failureContext );
						}
						return result;
					}
				},
				multiTenancyStrategy, queryOrchestrator
		);
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		if ( ElasticsearchBackend.class.isAssignableFrom( clazz ) ) {
			return (T) this;
		}
		throw log.backendUnwrappingWithUnknownType( clazz, ElasticsearchBackend.class, failureContext );
	}

	@Override
	public Backend toAPI() {
		return this;
	}

	@Override
	public <T> T getClient(Class<T> clientClass) {
		return client.unwrap( clientClass );
	}

	@Override
	public IndexManagerBuilder<ElasticsearchDocumentObjectBuilder> createIndexManagerBuilder(
			String hibernateSearchIndexName, boolean multiTenancyEnabled, BackendBuildContext buildContext, ConfigurationPropertySource propertySource) {
		if ( multiTenancyEnabled && !multiTenancyStrategy.isMultiTenancySupported() ) {
			throw log.multiTenancyRequiredButNotSupportedByBackend( hibernateSearchIndexName, failureContext );
		}

		String elasticsearchIndexName = ElasticsearchIndexNameNormalizer.normalize( hibernateSearchIndexName );
		String existingHibernateSearchIndexName = hibernateSearchIndexNamesByElasticsearchIndexNames.putIfAbsent(
				elasticsearchIndexName, hibernateSearchIndexName
		);
		if ( existingHibernateSearchIndexName != null ) {
			throw log.duplicateNormalizedIndexNames(
					existingHibernateSearchIndexName, hibernateSearchIndexName, elasticsearchIndexName,
					failureContext
			);
		}

		ElasticsearchIndexSchemaRootNodeBuilder indexSchemaRootNodeBuilder =
				new ElasticsearchIndexSchemaRootNodeBuilder( hibernateSearchIndexName, multiTenancyStrategy );

		return new ElasticsearchIndexManagerBuilder(
				indexingContext, searchContext,
				hibernateSearchIndexName, elasticsearchIndexName,
				indexSchemaRootNodeBuilder, buildContext, propertySource
		);
	}

	@Override
	public void close() {
		try ( Closer<IOException> closer = new Closer<>() ) {
			closer.push( ElasticsearchWorkOrchestrator::close, streamOrchestrator );
			closer.push( ElasticsearchWorkOrchestrator::close, queryOrchestrator );
			// Close the index writer after the orchestrators, when we're sure all works have been performed
			closer.push( ElasticsearchClient::close, client );
		}
		catch (IOException | RuntimeException e) {
			throw new SearchException( "Failed to shut down the Elasticsearch backend", e );
		}
	}

	@Override
	public String toString() {
		return new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "name=" ).append( name )
				.append( "]" )
				.toString();
	}
}
