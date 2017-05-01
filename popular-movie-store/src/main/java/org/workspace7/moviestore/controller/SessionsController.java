package org.workspace7.moviestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.session.MapSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.workspace7.moviestore.data.MovieCart;

/**
 * @author kameshs
 */
@RestController
@Slf4j
public class SessionsController {

    @Autowired
    CacheManager cacheManager;

    @GetMapping(value = "/sessions")
    public @ResponseBody
    String sessions() {
        AdvancedCache<String, Object> sessionCache = (AdvancedCache<String, Object>)
            cacheManager.getCache("moviestore-sessions-cache").getNativeCache();
        log.info("Sessions:{}", sessionCache.cacheEntrySet());

        ObjectMapper sessions = new ObjectMapper();

        ArrayNode sessionsArray = sessions.createArrayNode();

        if (sessionCache != null && !sessionCache.isEmpty()) {
            sessionCache.cacheEntrySet()
                .stream()
                .forEach(cacheEntry -> {
                    MapSession mapSession = (MapSession) cacheEntry.getValue();
                    ShoppingCartController cartController = mapSession.getAttribute("shoppingCartController");
                    MovieCart movieCart = cartController.getMovieCart();
                    log.debug("Shopping Card in Session {} is {}", mapSession.getId(), movieCart);

                    ObjectNode movieCartNode = sessions.createObjectNode();
                    movieCartNode.put("sessionId", mapSession.getId());
                    movieCartNode.put("orderId", movieCart.getOrderId());

                    ArrayNode movieItemsNode = movieCartNode.arrayNode();

                    movieCart.getMovieItems().forEach((movieId, qty) -> {
                        ObjectNode movieItem = movieItemsNode.addObject();
                        movieItem.put("movieId", movieId);
                        movieItem.put("orderQuantity", qty);
                        movieItemsNode.add(movieItem);
                    });

                    movieCartNode.set("items", movieItemsNode);

                    sessionsArray.add(movieCartNode);
                });
        }

        String jsonResponse = "{}";

        try {
            jsonResponse = sessions.writeValueAsString(sessionsArray);
        } catch (JsonProcessingException e) {
            log.error("Error building JSON response for sesisons", e);
        }

        return jsonResponse;
    }
}
