package org.workspace7.jdg;

import org.infinispan.client.hotrod.annotation.ClientCacheFailover;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheFailoverEvent;

/**
 * @author kameshs
 */
@ClientListener
public class MyClientListener {

    @ClientCacheFailover
    public void handleFailover(ClientCacheFailoverEvent event){
        System.out.println("Failover --- Event:" + event);
    }
}


