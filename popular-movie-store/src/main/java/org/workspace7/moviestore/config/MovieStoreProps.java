package org.workspace7.moviestore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kameshs
 */
@Component
@ConfigurationProperties(prefix = "moviestore")
@Data
public class MovieStoreProps {
    private String apiEndpointUrl;
    private String apiKey;
}
