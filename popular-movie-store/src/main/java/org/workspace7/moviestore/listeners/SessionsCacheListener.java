package org.workspace7.moviestore.listeners;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryInvalidatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

/**
 * @author kameshs
 */
@Listener(clustered = true)
@Slf4j
public class SessionsCacheListener {

    @CacheEntryCreated
    public void sessionCreated(CacheEntryCreatedEvent<Object, Object> event) {
        log.info("Session with id {} created : {}", event.getKey(), event.getValue());
    }

    @CacheEntryRemoved
    public void sessionRemoved(CacheEntryRemovedEvent<Object, Object> event) {
        log.info("Session with id {} removed : {}", event.getKey(), event.getValue());
    }

    @CacheEntryInvalidated
    public void sessionInvalidated(CacheEntryInvalidatedEvent<Object, Object> event) {
        log.info("Session with id {} invalidated : {}", event.getKey(), event.getValue());
    }
}
