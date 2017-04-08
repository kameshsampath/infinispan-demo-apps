package org.workspace7.jdg;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.fail;

/**
 * @author kameshs
 */
public class JDGSecurityTest {

    public static final String SECURITY_REALM = "ldap-security-realm";
    private RemoteCache testCache;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testWriteOnlyRole() {
        Configuration configuration = new ConfigurationBuilder()
                .connectionPool()
                .maxTotal(1)
                .security()
                .authentication()
                .enable()
                .serverName("node1-hotrod-server")
                .callbackHandler(new MyCallbackHandler("david",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers("192.168.56.1:11722")
                .build();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);
        testCache = remoteCacheManager.getCache("users-cache");
        testCache.put("testKey", "testWriteOnlyRole");
        try {
            testCache.get("testKey");
            fail("I should have access to put");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("ISPN000287: Unauthorized access", "lacks 'READ' permission");
        }
    }

    @Test
    public void testReadOnlyRole() {
        Configuration configuration = new ConfigurationBuilder()
                .connectionPool()
                .maxTotal(1)
                .security()
                .authentication()
                .enable()
                .serverName("192.168.56.1")
                .callbackHandler(new MyCallbackHandler("raja",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers("192.168.56.1:11722")
                .build();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);
        testCache = remoteCacheManager.getCache("users-cache");
        testCache.get("testKey");
        try {
            testCache.put("testKey2", "value2");
            fail("I should have access to put");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("ISPN000287: Unauthorized access", "lacks 'WRITE' permission");
        }
    }

    @Test
    public void testSupervisorRole() {
        Configuration configuration = new ConfigurationBuilder()
                .connectionPool()
                .maxTotal(1)
                .security()
                .authentication()
                .enable()
                .serverName("192.168.56.1")
                .callbackHandler(new MyCallbackHandler("admin",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers("192.168.56.1:11722")
                .build();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);
        testCache = remoteCacheManager.getCache("users-cache");
        testCache.put("testKey3", "value3");
        assertThat(testCache.get("testKey3")).isNotNull();
        assertThat(testCache.get("testKey3")).isEqualTo("value3");
    }
}
