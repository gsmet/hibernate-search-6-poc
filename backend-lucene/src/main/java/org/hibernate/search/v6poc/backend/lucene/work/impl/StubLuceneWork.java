/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.backend.lucene.work.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.hibernate.search.v6poc.backend.lucene.logging.impl.Log;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * @author Guillaume Smet
 */
public class StubLuceneWork<T> implements LuceneWork<T> {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final String workType;

	private final Document document;

	private final Query query;

	private final Map<String, List<String>> parameters;

	public StubLuceneWork(String workType) {
		this.workType = workType;
		this.document = null;
		this.query = null;
		this.parameters = new LinkedHashMap<>();
	}

	public StubLuceneWork(String workType, Document document) {
		this.workType = workType;
		this.document = document;
		this.query = null;
		this.parameters = new LinkedHashMap<>();
	}

	public StubLuceneWork(String workType, Query query) {
		this.workType = workType;
		this.document = null;
		this.query = query;
		this.parameters = new LinkedHashMap<>();
	}

	public StubLuceneWork<T> addParam(String name, String value) {
		if ( value != null ) {
			parameters.computeIfAbsent( name, ignored -> new ArrayList<>() ).add( value );
		}
		return this;
	}

	public <U> StubLuceneWork<T> addParam(String name, U value, Function<U, String> renderer) {
		if ( value != null ) {
			parameters.computeIfAbsent( name, ignored -> new ArrayList<>() ).add( renderer.apply( value ) );
		}
		return this;
	}

	public StubLuceneWork<T> addParam(String name, Collection<String> values) {
		if ( values != null && values.stream().anyMatch( Objects::nonNull ) ) {
			parameters.computeIfAbsent( name, ignored -> new ArrayList<>() ).addAll( values );
		}
		return this;
	}

	// XXX GSM implement setResultFunction once we know more about Lucene search results extraction

	@Override
	public CompletableFuture<T> execute(LuceneWorkExecutionContext context) {
		log.infov( "Executing work: %1$s", this );
		return CompletableFuture.completedFuture( null );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "type=" ).append( workType );
		if ( document != null ) {
			sb.append( ", document=" ).append( document );
		}
		if ( query != null ) {
			sb.append( ", query=" ).append( query );
		}
		sb.append( "]" );
		return sb.toString();
	}
}
