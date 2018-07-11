/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.logging.spi;

import java.util.Set;

import org.hibernate.search.v6poc.entity.model.spi.MappableTypeModel;
import org.hibernate.search.v6poc.logging.impl.EngineEventContextMessages;
import org.hibernate.search.v6poc.util.EventContext;
import org.hibernate.search.v6poc.util.EventContextElement;
import org.hibernate.search.v6poc.util.impl.common.CollectionHelper;

import org.jboss.logging.Messages;

public class EventContexts {

	private static final EngineEventContextMessages MESSAGES = Messages.getBundle( EngineEventContextMessages.class );

	private static final EventContext DEFAULT = EventContext.create(
			new EventContextElement() {
				@Override
				public String toString() {
					return "EventContextElement[" + render() + "]";
				}

				@Override
				public String render() {
					return MESSAGES.defaultOnMissingContextElement();
				}
			}
	);

	private static final EventContext INDEX_SCHEMA_ROOT = EventContext.create(
			new EventContextElement() {
				@Override
				public String toString() {
					return "EventContextElement[" + render() + "]";
				}

				@Override
				public String render() {
					return MESSAGES.indexSchemaRoot();
				}
			}
	);

	private EventContexts() {
	}

	public static EventContext getDefault() {
		return DEFAULT;
	}

	public static EventContext fromType(MappableTypeModel typeModel) {
		return EventContext.create( new AbstractSimpleEventContextElement<MappableTypeModel>( typeModel ) {
			@Override
			public String render(MappableTypeModel param) {
				String typeName = param.getName();
				return MESSAGES.type( typeName );
			}
		} );
	}

	public static EventContext fromBackendName(String name) {
		return EventContext.create( new AbstractSimpleEventContextElement<String>( name ) {
			@Override
			public String render(String param) {
				return MESSAGES.backend( param );
			}
		} );
	}

	public static EventContext fromIndexName(String name) {
		return EventContext.create( new AbstractSimpleEventContextElement<String>( name ) {
			@Override
			public String render(String param) {
				return MESSAGES.index( param );
			}
		} );
	}
	public static EventContext fromIndexNames(String ... indexNames) {
		return fromIndexNames( CollectionHelper.asLinkedHashSet( indexNames ) );
	}

	public static EventContext fromIndexNames(Set<String> indexNames) {
		return EventContext.create( new AbstractSimpleEventContextElement<Set<String>>( indexNames ) {
			@Override
			public String render(Set<String> indexNames) {
				return MESSAGES.indexes( indexNames );
			}
		} );
	}


	public static EventContext indexSchemaRoot() {
		return INDEX_SCHEMA_ROOT;
	}

	public static EventContext fromIndexFieldAbsolutePath(String absolutePath) {
		return EventContext.create( new AbstractSimpleEventContextElement<String>( absolutePath ) {
			@Override
			public String render(String param) {
				return MESSAGES.indexFieldAbsolutePath( param );
			}
		} );
	}

}
