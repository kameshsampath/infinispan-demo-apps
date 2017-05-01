package org.workspace7.moviestore.listeners;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

import java.util.concurrent.CountDownLatch;

/**
 * @author kameshs
 */
@Listener
@Slf4j
public class ClusterListener {

    CountDownLatch clusterFormedLatch = new CountDownLatch(1);
    CountDownLatch shutdownLatch = new CountDownLatch(1);
    private final int expectedNodes;

    public ClusterListener(int expectedNodes) {
        this.expectedNodes = expectedNodes;
    }

    @ViewChanged
    public void viewChanged(ViewChangedEvent event) {
        log.info("PMS:::View changed: {} ", event.getNewMembers());
        if (event.getCacheManager().getMembers().size() == expectedNodes) {
            clusterFormedLatch.countDown();
        } else if (event.getNewMembers().size() < event.getOldMembers().size()) {
            shutdownLatch.countDown();
        }
    }
}
