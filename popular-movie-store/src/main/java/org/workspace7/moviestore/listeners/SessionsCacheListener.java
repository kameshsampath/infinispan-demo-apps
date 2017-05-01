package org.workspace7.moviestore.listeners;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryExpired;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryExpiredEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryInvalidatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

/**
 * @author kameshs
 */
@Listener
@Slf4j
public class SessionsCacheListener {

    @CacheEntryCreated
    public void sessionCreated(CacheEntryCreatedEvent event) {
        log.info("Session with id {} created with data {} ", event.getKey(), event.getValue());
    }

    @CacheEntryInvalidated
    public void sessionInvalidated(CacheEntryInvalidatedEvent event) {
        log.info("Session with id {} invalidated ", event.getKey());
    }

    @CacheEntryRemoved
    public void sessionEvicted(CacheEntryRemovedEvent event) {
        log.info("Session with id {} evicted ", event.getKey());
    }

    @CacheEntryExpired
    public void sessionExpired(CacheEntryExpiredEvent event) {
        log.info("Session with id {} expired ", event.getKey());
    }
}
