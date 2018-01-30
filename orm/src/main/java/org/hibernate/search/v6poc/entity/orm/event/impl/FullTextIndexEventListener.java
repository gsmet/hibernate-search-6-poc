/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.orm.event.impl;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Session;
import org.hibernate.event.spi.AbstractCollectionEvent;
import org.hibernate.event.spi.FlushEvent;
import org.hibernate.event.spi.FlushEventListener;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;
import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.search.v6poc.entity.orm.impl.HibernateSearchContextService;
import org.hibernate.search.v6poc.entity.orm.logging.impl.Log;
import org.hibernate.search.v6poc.util.spi.LoggerFactory;

/**
 * Hibernate ORM event listener called by various ORM life cycle events. This listener must be registered in order
 * to enable automatic index updates.
 *
 * @author Gavin King
 * @author Emmanuel Bernard
 * @author Mattias Arbin
 * @author Sanne Grinovero
 * @author Hardy Ferentschik
 */
public final class FullTextIndexEventListener implements PostDeleteEventListener,
		PostInsertEventListener, PostUpdateEventListener,
		PostCollectionRecreateEventListener, PostCollectionRemoveEventListener,
		PostCollectionUpdateEventListener, FlushEventListener {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final boolean eventProcessingEnabled;
	private final boolean dirtyCheckingEnabled;

	private volatile EventsHibernateSearchState state = new NonInitializedHibernateSearchState();

	public FullTextIndexEventListener(boolean eventProcessingEnabled, boolean dirtyCheckingEnabled) {
		this.eventProcessingEnabled = eventProcessingEnabled;
		this.dirtyCheckingEnabled = dirtyCheckingEnabled;
	}

	// TODO handle the "simulated" transaction when a Flush listener is registered
	//only used by the FullTextIndexEventListener instance playing in the FlushEventListener role.
	// make sure the Synchronization doesn't contain references to Session, otherwise we'll leak memory.
//	private final Map<Session, Synchronization> flushSynch = Maps.createIdentityWeakKeyConcurrentMap( 64, 32 );

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if ( !eventProcessingEnabled ) {
			return;
		}

		HibernateSearchContextService context = state.getHibernateSearchContext();
		final Object entity = event.getEntity();
		if ( isInstanceOfIndexedType( context, entity ) ) {
			// TODO Check whether deletes work with hibernate.use_identifier_rollback enabled (see HSEARCH-650)
			// I think they should, but better safe than sorry
			context.getCurrentWorker( event.getSession() )
					.delete( event.getId(), entity );
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if ( !eventProcessingEnabled ) {
			return;
		}

		HibernateSearchContextService context = state.getHibernateSearchContext();
		final Object entity = event.getEntity();
		if ( isInstanceOfIndexedType( context, entity ) ) {
			context.getCurrentWorker( event.getSession() )
					.add( event.getId(), entity );
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if ( !eventProcessingEnabled ) {
			return;
		}

		HibernateSearchContextService context = state.getHibernateSearchContext();
		final Object entity = event.getEntity();
		if ( isInstanceOfIndexedType( context, entity ) &&
				( !dirtyCheckingEnabled || true ) ) { // TODO implement dirty checking
			context.getCurrentWorker( event.getSession() )
					.update( event.getId(), entity );
		}
	}

	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		processCollectionEvent( event );
	}

	@Override
	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
		processCollectionEvent( event );
	}

	@Override
	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
		processCollectionEvent( event );
	}

	/**
	 * Make sure the indexes are updated right after the hibernate flush,
	 * avoiding object loading during a flush. Not needed during transactions.
	 */
	@Override
	public void onFlush(FlushEvent event) {
		if ( !eventProcessingEnabled ) {
			return;
		}

		Session session = event.getSession();
		// TODO handle the "simulated" transaction when a Flush listener is registered
//		Synchronization synchronization = flushSynch.get( session );
//		if ( synchronization != null ) {
//			//first cleanup
//			flushSynch.remove( session );
//			log.debug( "flush event causing index update out of transaction" );
//			synchronization.beforeCompletion();
//			synchronization.afterCompletion( Status.STATUS_COMMITTED );
//		}
	}

	/**
	 * Initialize method called by Hibernate Core when the SessionFactory starts.
	 * @param contextFuture a completable future that will eventually hold the initialized {@link HibernateSearchContextService}
	 */
	public void initialize(CompletableFuture<HibernateSearchContextService> contextFuture) {
		this.state = new InitializingHibernateSearchState( contextFuture.thenApply( this::doInitialize ) );
	}

	private HibernateSearchContextService doInitialize(HibernateSearchContextService context) {
		log.debug( "Hibernate Search event listeners " + ( eventProcessingEnabled ? "activated" : "deactivated" ) );
		if ( eventProcessingEnabled ) {
			log.debug( "Hibernate Search dirty checks " + ( dirtyCheckingEnabled ? "enabled" : "disabled" ) );
		}
		OptimalEventsHibernateSearchState newState = new OptimalEventsHibernateSearchState( context );
		this.state = newState; // discard the suboptimal EventsHibernateSearchState instances
		return context;
	}

	private boolean isInstanceOfIndexedType(HibernateSearchContextService context, Object entity) {
		return context.getMapping().isIndexable( entity );
	}

	// TODO handle the "simulated" transaction when a Flush listener is registered
//	/**
//	 * Adds a synchronization to be performed in the onFlush method;
//	 * should only be used as workaround for the case a flush is happening
//	 * out of transaction.
//	 * Warning: if the synchronization contains a hard reference
//	 * to the Session proper cleanup is not guaranteed and memory leaks
//	 * will happen.
//	 *
//	 * @param eventSource should be the Session doing the flush
//	 * @param synchronization the synchronisation instance
//	 */
//	public void addSynchronization(EventSource eventSource, Synchronization synchronization) {
//		this.flushSynch.put( eventSource, synchronization );
//	}

	protected void processCollectionEvent(AbstractCollectionEvent event) {
		if ( eventProcessingEnabled ) {
			return;
		}

		// TODO ContainedIn/IndexedEmbedded
//		Object entity = event.getAffectedOwnerOrNull();
//		if ( entity == null ) {
//			//Hibernate cannot determine every single time the owner especially in case detached objects are involved
//			// or property-ref is used
//			//Should log really but we don't know if we're interested in this collection for indexing
//			return;
//		}
//		PersistentCollection persistentCollection = event.getCollection();
//		final String collectionRole;
//		if ( persistentCollection != null ) {
//			collectionRole = persistentCollection.getRole();
//		}
//		else {
//			collectionRole = null;
//		}
//		AbstractDocumentBuilder documentBuilder = getDocumentBuilder( entity );
//
//		if ( documentBuilder != null && documentBuilder.collectionChangeRequiresIndexUpdate( collectionRole ) ) {
//			Serializable id = getId( entity, event );
//			if ( id == null ) {
//				log.idCannotBeExtracted( event.getAffectedOwnerEntityName() );
//				return;
//			}
//			processWork( tenantIdentifier( event ), entity, id, WorkType.COLLECTION, event, false );
//		}
//	}
//
//	private Serializable getId(Object entity, AbstractCollectionEvent event) {
//		Serializable id = event.getAffectedOwnerIdOrNull();
//		if ( id == null ) {
//			// most likely this recovery is unnecessary since Hibernate Core probably try that
//			EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry( entity );
//			id = entityEntry == null ? null : entityEntry.getId();
//		}
//		return id;
	}

	/**
	 * Required since Hibernate ORM 4.3
	 */
	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		// TODO Tests seem to pass using _false_ but we might be able to take
		// advantage of this new hook?
		return false;
	}

}
