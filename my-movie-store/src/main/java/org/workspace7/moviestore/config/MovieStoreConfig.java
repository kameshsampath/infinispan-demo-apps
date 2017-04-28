package org.workspace7.moviestore.config;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.infinispan.spring.session.configuration.EnableInfinispanEmbeddedHttpSession;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author kameshs
 */
@Configuration
@EnableInfinispanEmbeddedHttpSession
@EnableCaching
@EnableConfigurationProperties(MovieStoreProps.class)
public class MovieStoreConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() throws IOException {
        return new SpringEmbeddedCacheManager(infinispanCacheManager());

    }

    private EmbeddedCacheManager infinispanCacheManager() throws IOException {
        return new DefaultCacheManager("infinispan-moviestore.xml");
    }
}
