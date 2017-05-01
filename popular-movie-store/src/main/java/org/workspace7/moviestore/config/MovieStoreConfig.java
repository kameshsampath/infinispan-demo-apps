package org.workspace7.moviestore.config;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.infinispan.spring.session.configuration.EnableInfinispanEmbeddedHttpSession;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.workspace7.moviestore.listeners.SessionsCacheListener;

import java.io.IOException;

/**
 * @author kameshs
 */
@EnableInfinispanEmbeddedHttpSession(cacheName = "moviestore-sessions-cache")
@Configuration
@EnableCaching
@EnableConfigurationProperties(MovieStoreProps.class)
public class MovieStoreConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SpringEmbeddedCacheManager cacheManager() throws IOException {
        return new SpringEmbeddedCacheManager(infinispanCacheManager());
    }

    public EmbeddedCacheManager infinispanCacheManager() throws IOException {
        EmbeddedCacheManager embeddedCacheManager = new DefaultCacheManager(this.getClass()
            .getResourceAsStream("/infinispan-moviestore.xml"));
        embeddedCacheManager
            .getCache("moviestore-sessions-cache")
            .addListener(new SessionsCacheListener());
        return embeddedCacheManager;
    }

}
