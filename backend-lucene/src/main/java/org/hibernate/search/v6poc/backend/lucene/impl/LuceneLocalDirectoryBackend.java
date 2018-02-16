/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.impl;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hibernate.search.v6poc.backend.index.spi.IndexManagerBuilder;
import org.hibernate.search.v6poc.backend.lucene.document.impl.LuceneDocumentBuilder;
import org.hibernate.search.v6poc.backend.lucene.index.impl.LuceneLocalDirectoryIndexManagerBuilder;
import org.hibernate.search.v6poc.backend.lucene.logging.impl.Log;
import org.hibernate.search.v6poc.backend.lucene.orchestration.impl.LuceneWorkOrchestrator;
import org.hibernate.search.v6poc.backend.lucene.orchestration.impl.StubLuceneWorkOrchestrator;
import org.hibernate.search.v6poc.backend.lucene.work.impl.LuceneWorkFactory;
import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;
import org.hibernate.search.v6poc.engine.spi.BuildContext;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * @author Guillaume Smet
 */
public class LuceneLocalDirectoryBackend implements LuceneBackend {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final String name;

	private final Path rootDirectory;

	private final LuceneWorkFactory workFactory;

	private final LuceneWorkOrchestrator streamOrchestrator;

	private final LuceneWorkOrchestrator queryOrchestrator;

	public LuceneLocalDirectoryBackend(String name, Path rootDirectory, LuceneWorkFactory workFactory) {
		this.name = name;
		this.rootDirectory = rootDirectory;

		this.workFactory = workFactory;
		this.streamOrchestrator = new StubLuceneWorkOrchestrator();
		this.queryOrchestrator = new StubLuceneWorkOrchestrator();

		initializeRootDirectory( name, rootDirectory );
	}

	@Override
	public String normalizeIndexName(String rawIndexName) {
		return rawIndexName;
	}

	@Override
	public IndexManagerBuilder<LuceneDocumentBuilder> createIndexManagerBuilder(
			String name, BuildContext context, ConfigurationPropertySource propertySource) {
		return new LuceneLocalDirectoryIndexManagerBuilder( this, name, context, propertySource );
	}

	@Override
	public LuceneWorkFactory getWorkFactory() {
		return workFactory;
	}

	@Override
	public LuceneWorkOrchestrator createChangesetOrchestrator() {
		return new StubLuceneWorkOrchestrator();
	}

	@Override
	public LuceneWorkOrchestrator getStreamOrchestrator() {
		return streamOrchestrator;
	}

	@Override
	public LuceneWorkOrchestrator getQueryOrchestrator() {
		return queryOrchestrator;
	}

	@Override
	public void close() {
	}

	@Override
	public String toString() {
		return new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "name=" ).append( name ).append( ", " )
				.append( "rootDirectory=" ).append( rootDirectory )
				.append( "]" )
				.toString();
	}

	private static void initializeRootDirectory(String name, Path rootDirectory) {
		if ( Files.exists( rootDirectory ) ) {
			if ( !Files.isDirectory( rootDirectory ) || !Files.isWritable( rootDirectory ) ) {
				throw log.localDirectoryBackendRootDirectoryNotWritableDirectory( name, rootDirectory );
			}
		}
		else {
			try {
				Files.createDirectory( rootDirectory );
			}
			catch (Exception e) {
				throw log.unableToCreateRootDirectoryForLocalDirectoryBackend( name, rootDirectory, e );
			}
		}
	}
}
