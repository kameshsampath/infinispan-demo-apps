package org.workspace7.moviestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author kameshs
 */
@SpringBootApplication
@EnableAutoConfiguration
public class MyMovieStoreApp extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(MyMovieStoreApp.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyMovieStoreApp.class);
    }
}
