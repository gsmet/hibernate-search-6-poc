/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.mapping.building.spi;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.hibernate.search.v6poc.entity.model.spi.IndexableTypeOrdering;
import org.hibernate.search.v6poc.entity.model.spi.IndexedTypeIdentifier;
import org.hibernate.search.v6poc.entity.pojo.model.impl.PojoIndexedTypeIdentifier;


/**
 * @author Yoann Rodiere
 */
public final class PojoTypeOrdering implements IndexableTypeOrdering {

	private static final PojoTypeOrdering INSTANCE = new PojoTypeOrdering();

	public static PojoTypeOrdering get() {
		return INSTANCE;
	}

	private PojoTypeOrdering() {
	}

	@Override
	public boolean isSubType(IndexedTypeIdentifier parent, IndexedTypeIdentifier subType) {
		return toClass( parent ).isAssignableFrom( toClass( subType ) );
	}

	@Override
	public Stream<IndexedTypeIdentifier> getAscendingSuperTypes(IndexedTypeIdentifier subType) {
		return getAscendingSuperTypes( toClass( subType ) ).map( this::toId );
	}

	@Override
	public Stream<IndexedTypeIdentifier> getDescendingSuperTypes(IndexedTypeIdentifier subType) {
		return getDescendingSuperTypes( toClass( subType ) ).map( this::toId );
	}

	public Stream<Class<?>> getAscendingSuperTypes(Class<?> subType) {
		// Use a LinkedHashSet to preserve order while still providing efficient element lookup
		Set<Class<?>> result = new LinkedHashSet<>();
		collectSuperTypesAscending( result, subType );
		return result.stream();
	}

	public Stream<Class<?>> getDescendingSuperTypes(Class<?> subType) {
		// Use a LinkedHashSet to preserve order while still providing efficient element lookup
		Set<Class<?>> result = new LinkedHashSet<>();
		collectSuperTypesDescending( result, subType );
		return result.stream();
	}

	private void collectSuperTypesAscending(Set<Class<?>> result, Class<?> subType) {
		if ( ! result.add( subType ) ) {
			// We've already seen this type, skip the rest of this method
		}
		for ( Class<?> interfaze : subType.getInterfaces() ) {
			collectSuperTypesAscending( result, interfaze );
		}
		if ( !Object.class.equals( subType ) ) {
			collectSuperTypesAscending( result, subType.getSuperclass() );
		}
	}

	private void collectSuperTypesDescending(Set<Class<?>> result, Class<?> subType) {
		if ( result.contains( subType ) ) {
			// We've already seen this type, skip the rest of this method
		}
		if ( !Object.class.equals( subType ) ) {
			collectSuperTypesDescending( result, subType.getSuperclass() );
		}
		for ( Class<?> interfaze : subType.getInterfaces() ) {
			collectSuperTypesDescending( result, interfaze );
		}
		result.add( subType );
	}

	private Class<?> toClass(IndexedTypeIdentifier typeId) {
		return ( (PojoIndexedTypeIdentifier) typeId ).toJavaType();
	}

	private PojoIndexedTypeIdentifier toId(Class<?> clazz) {
		return new PojoIndexedTypeIdentifier( clazz );
	}

}
