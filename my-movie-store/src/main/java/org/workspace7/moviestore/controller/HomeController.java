package org.workspace7.moviestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/sessions")
    public String sessions(Map<String, Object> model) {
        AdvancedCache<String, Object> sessionCache = (AdvancedCache<String, Object>)
            cacheManager.getCache("moviestore-sessions-cache").getNativeCache();
        log.info("Sessions:{}", sessionCache.cacheEntrySet());
        List<Object> sessions = new ArrayList<>();
        if (sessionCache != null && !sessionCache.isEmpty()) {
            sessionCache.cacheEntrySet()
                .stream()
                .forEach(cacheEntry -> {
                    MapSession mapSession = (MapSession) cacheEntry.getValue();
                    ShoppingCartController cartController = mapSession.getAttribute("shoppingCartController");

                    //mapSession.getAttributeNames().stream().forEach(s -> log.info("Session attr:{}", s));
                    log.info("Shopping Card in Session {} is {}", mapSession.getId(), cartController.getMovieCart());

                    sessions.add(cacheEntry.getValue());
                });
        }
        model.put("sessions", sessions);
        return "sessions";
    }
}
