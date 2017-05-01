package org.workspace7.moviestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.infinispan.stream.CacheCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.session.MapSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.workspace7.moviestore.data.MovieCart;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO fix json building...
 *
 * @author kameshs
 */
@RestController
@Slf4j
public class SessionsController {

    @Autowired
    CacheManager cacheManager;

    @GetMapping(value = "/sessions")
    public @ResponseBody
    String sessions(HttpServletRequest request) {

        String jsonResponse = "NO SESSIONS AVAILABLE";

        try {

            AdvancedCache<String, Object> sessionCache = (AdvancedCache<String, Object>)
                cacheManager.getCache("moviestore-sessions-cache").getNativeCache();

            if (sessionCache != null && !sessionCache.isEmpty()) {

                ObjectMapper sessions = new ObjectMapper();

                ArrayNode sessionsArray = sessions.createArrayNode();

                Map<String, Object> sessionsCacheMap = sessionCache.entrySet()
                    .stream()
                    .collect(CacheCollectors.serializableCollector(() ->
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

                sessionsCacheMap.forEach((s, o) -> {

                    MapSession mapSession = (MapSession) o;

                    log.debug("Session Controller Map Session Id {} value : {}", s, mapSession);

                    if (log.isDebugEnabled()) {
                        StringBuilder debugMessage = new StringBuilder();

                        mapSession.getAttributeNames().forEach(key -> {
                            debugMessage.append("Attribute :" + s + " Value: " + mapSession.getAttribute(key));
                        });

                        log.debug("Map Session Attributes : {}", debugMessage);
                    }

                    MovieCart movieCart = mapSession.getAttribute(ShoppingCartController.SESSION_ATTR_MOVIE_CART);

                    if (movieCart != null) {

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

                        movieCartNode.set("movies", movieItemsNode);

                        sessionsArray.add(movieCartNode);
                    }
                });
                jsonResponse = sessions.writeValueAsString(sessionsArray);
            }

        } catch (Exception e) {
            log.error("Error building JSON response for sesisons", e);
        }

        return jsonResponse;
    }

}
