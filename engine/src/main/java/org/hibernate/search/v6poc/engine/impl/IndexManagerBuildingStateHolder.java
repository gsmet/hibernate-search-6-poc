/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.engine.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.search.v6poc.backend.document.model.spi.IndexModelCollectorImplementor;
import org.hibernate.search.v6poc.backend.document.spi.DocumentState;
import org.hibernate.search.v6poc.backend.index.impl.SimplifyingIndexManager;
import org.hibernate.search.v6poc.backend.index.spi.IndexManager;
import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.backend.spi.Backend;
import org.hibernate.search.v6poc.backend.spi.BackendFactory;
import org.hibernate.search.v6poc.bridge.impl.BridgeFactory;
import org.hibernate.search.v6poc.bridge.impl.BridgeReferenceResolver;
import org.hibernate.search.v6poc.engine.spi.BeanResolver;
import org.hibernate.search.v6poc.engine.spi.BuildContext;
import org.hibernate.search.v6poc.entity.mapping.building.impl.MappingIndexModelCollectorImpl;
import org.hibernate.search.v6poc.entity.mapping.building.spi.IndexManagerBuildingState;
import org.hibernate.search.v6poc.entity.mapping.building.spi.MappingIndexModelCollector;
import org.hibernate.search.v6poc.entity.model.spi.IndexableTypeOrdering;
import org.hibernate.search.v6poc.util.SearchException;
import org.hibernate.search.v6poc.util.spi.MaskedProperty;


/**
 * @author Yoann Rodiere
 */
// TODO close every backend built so far (which should close index managers) in case of failure
public class IndexManagerBuildingStateHolder {

	private final BuildContext buildContext;
	private final Properties properties;
	private final Properties defaultIndexProperties;
	private final BridgeFactory bridgeFactory;
	private final BridgeReferenceResolver bridgeReferenceResolver;

	private final Map<String, Backend<?>> backendsByName = new HashMap<>();
	private final Map<String, IndexMappingBuildingStateImpl<?>> indexManagerBuildingStateByName = new HashMap<>();

	public IndexManagerBuildingStateHolder(BuildContext buildContext,
			Properties properties, BridgeFactory bridgeFactory,
			BridgeReferenceResolver bridgeReferenceResolver) {
		this.buildContext = buildContext;
		this.properties = properties;
		this.defaultIndexProperties = new MaskedProperty( properties, "index.default" );
		this.bridgeFactory = bridgeFactory;
		this.bridgeReferenceResolver = bridgeReferenceResolver;
	}

	public IndexManagerBuildingState<?> startBuilding(String indexName, IndexableTypeOrdering typeOrdering) {
		return indexManagerBuildingStateByName.compute(
				indexName,
				(k, v) -> {
					if ( v == null ) {
						Properties indexProperties = new MaskedProperty( properties, "index." + indexName, defaultIndexProperties );
						return createIndexManagerBuildingState( indexName, indexProperties, typeOrdering );
					}
					else {
						throw new SearchException( "Multiple entity mappings target the same index, which is forbidden" );
					}
				} );
	}

	private IndexMappingBuildingStateImpl<?> createIndexManagerBuildingState(
			String indexName, Properties indexProperties, IndexableTypeOrdering typeOrdering) {
		// TODO more checks on the backend name (is non-null, non-empty)
		String backendName = indexProperties.getProperty( "backend" );
		Backend<?> backend = backendsByName.computeIfAbsent( backendName, this::createBackend );
		return createIndexManagerBuildingState( backend, indexName, indexProperties, typeOrdering );
	}

	private <D extends DocumentState> IndexMappingBuildingStateImpl<D> createIndexManagerBuildingState(
			Backend<D> backend, String indexName, Properties indexProperties, IndexableTypeOrdering typeOrdering) {
		IndexManagerBuilder<D> builder = backend.createIndexManagerBuilder( indexName, buildContext, indexProperties );
		IndexModelCollectorImplementor modelCollector = builder.getModelCollector();
		MappingIndexModelCollectorImpl mappingModelCollector = new MappingIndexModelCollectorImpl(
				bridgeFactory, bridgeReferenceResolver, modelCollector, typeOrdering );
		return new IndexMappingBuildingStateImpl<>( indexName, builder, mappingModelCollector );
	}

	private Backend<?> createBackend(String backendName) {
		Properties backendProperties = new MaskedProperty( properties, "backend." + backendName );
		// TODO more checks on the backend type (non-null, non-empty)
		String backendType = backendProperties.getProperty( "type" );

		BeanResolver beanResolver = buildContext.getServiceManager().getBeanResolver();
		BackendFactory backendFactory = beanResolver.resolve( backendType, BackendFactory.class );
		return backendFactory.create( backendName, buildContext, backendProperties );
	}

	public Map<String, IndexManager<?>> build() {
		Map<String, IndexManager<?>> result = new HashMap<>();
		indexManagerBuildingStateByName.forEach( (k, v) -> result.put( k, v.build() ) );
		// TODO close the managers created so far if anything fails
		return result;
	}

	private static class IndexMappingBuildingStateImpl<D extends DocumentState> implements IndexManagerBuildingState<D> {

		private final String indexName;
		private final IndexManagerBuilder<D> builder;
		private final MappingIndexModelCollector modelCollector;
		private IndexManager<D> result;

		public IndexMappingBuildingStateImpl(String indexName,
				IndexManagerBuilder<D> builder,
				MappingIndexModelCollector modelCollector) {
			this.indexName = indexName;
			this.builder = builder;
			this.modelCollector = modelCollector;
		}

		@Override
		public String getIndexName() {
			return indexName;
		}

		@Override
		public MappingIndexModelCollector getModelCollector() {
			return modelCollector;
		}

		@Override
		public IndexManager<D> getResult() {
			if ( result == null ) {
				throw new SearchException( "getResult() called before the IndexManager was built" );
			}
			return result;
		}

		public IndexManager<D> build() {
			result = builder.build();
			// Optimize changeset execution in the resulting index manager
			result = new SimplifyingIndexManager<>( result );
			return result;
		}
	}

}
