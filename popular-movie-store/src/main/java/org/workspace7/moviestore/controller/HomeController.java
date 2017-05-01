package org.workspace7.moviestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.workspace7.moviestore.utils.MovieDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kameshs
 */
@Controller
@Slf4j
public class HomeController {

    @Autowired
    MovieDBHelper movieDBHelper;

    @Autowired
    CacheManager cacheManager;

    @GetMapping("/")
    public String home(Map<String, Object> model) {
        model.put("movies", movieDBHelper.getAll());
        return "home";
    }

    @GetMapping("/healthz")
    @ResponseStatus(HttpStatus.OK)
    public void healthz(Map<String, Object> model) {
        log.trace("Health check seems to be good...");
    }

}
