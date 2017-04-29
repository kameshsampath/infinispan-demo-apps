package org.workspace7.moviestore.config;

import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.infinispan.spring.session.configuration.EnableInfinispanEmbeddedHttpSession;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

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
    public SpringEmbeddedCacheManagerFactoryBean springCache() {
        SpringEmbeddedCacheManagerFactoryBean factoryBean = new SpringEmbeddedCacheManagerFactoryBean();
        factoryBean.setConfigurationFileLocation(new ClassPathResource("/infinispan-moviestore.xml"));
        return factoryBean;
    }
}
