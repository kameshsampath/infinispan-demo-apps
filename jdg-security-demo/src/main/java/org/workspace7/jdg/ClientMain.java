package org.workspace7.jdg;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.exceptions.TransportException;

/**
 * @author kameshs
 */
public class ClientMain {


    public static final String SECURITY_REALM = "ldap-security-realm";

    public static void main(String[] args) {

        MyClientListener myClientListener = new MyClientListener();

        RemoteCache testCache = null;

        try {

            Configuration configuration = new ConfigurationBuilder()
                    .connectionPool()
                    .maxTotal(1)
                    .security()
                    .authentication()
                    .enable()
                    .serverName("192.168.56.1")
                    .callbackHandler(new MyCallbackHandler(args[0],
                            SECURITY_REALM, args[1].toCharArray()))
                    .saslMechanism("DIGEST-MD5")
                    .addServers("192.168.56.1:11722")
                    .build();

            RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);

            testCache = remoteCacheManager.getCache("users-cache");
            testCache.getVersion();
            //for (int i = 0; i < 10; i++) {
            //testCache.addClientListener(myClientListener);
            //testCache.put("Key-One", "10001");
            System.out.printf("Key:[%s] has value [%s]", "Key One", testCache.get("Key-One"));
            testCache.remove("Key-One");
            //}

            remoteCacheManager.stop();

        } catch (TransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //testCache.removeClientListener(myClientListener);
        }
    }
}
