package org.workspace7.jdg;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

    public static final Properties jdgProperties = new Properties();

    @BeforeClass
    public static void loadProperties() {
        try {
            jdgProperties.load(JDGSecurityTest.class.getResourceAsStream("/jdg.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteOnlyRole() {
        assertThat(jdgProperties).isNotNull();
        assertThat(jdgProperties).isNotEmpty();
        assertThat(jdgProperties).containsKey("infinispan.client.hotrod.server_list");
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
                .addServers(jdgProperties.getProperty("infinispan.client.hotrod.server_list"))
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
                .serverName("node1-hotrod-server")
                .callbackHandler(new MyCallbackHandler("raja",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers(jdgProperties.getProperty("infinispan.client.hotrod.server_list"))
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
    public void testManagersRole() {
        Configuration configuration = new ConfigurationBuilder()
                .connectionPool()
                .maxTotal(1)
                .security()
                .authentication()
                .enable()
                .serverName("node1-hotrod-server")
                .callbackHandler(new MyCallbackHandler("james",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers(jdgProperties.getProperty("infinispan.client.hotrod.server_list"))
                .build();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);
        testCache = remoteCacheManager.getCache("users-cache");
        testCache.put("testKey3", "value3");
        assertThat(testCache.get("testKey3")).isNotNull();
        assertThat(testCache.get("testKey3")).isEqualTo("value3");
    }

    //ADD test to check admin role

    @Test
    public void testClusterAdminsRole() {
        Map<String,String> values = new HashMap<>();
        values.put("key25","25");
        values.put("key26","26");
        values.put("key27","27");
        Configuration configuration = new ConfigurationBuilder()
                .connectionPool()
                .maxTotal(1)
                .security()
                .authentication()
                .enable()
                .serverName("node1-hotrod-server")
                .callbackHandler(new MyCallbackHandler("admin",
                        SECURITY_REALM, "password".toCharArray()))
                .saslMechanism("PLAIN")
                .addServers(jdgProperties.getProperty("infinispan.client.hotrod.server_list"))
                .build();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configuration);
        testCache = remoteCacheManager.getCache("users-cache");
        testCache.put("testKey3", "value3"); //single-put operation
        assertThat(testCache.get("testKey3")).isNotNull();
        assertThat(testCache.get("testKey3")).isEqualTo("value3");
        testCache.putAll(values); // bulk write
        Set<String> keys = testCache.keySet(); //bulk read
        assertThat(keys).contains("key25","key26","key27");
        testCache.clear(); // bulk write again

        //TODO exec
    }
}
