package org.workspace7.moviestore.controller;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.session.MapSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.workspace7.moviestore.data.MovieCart;

import javax.servlet.http.HttpServletRequest;

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

        AdvancedCache<String, Object> sessionCache = (AdvancedCache<String, Object>)
            cacheManager.getCache("moviestore-sessions-cache").getNativeCache();

        log.info("Sessions:{}", sessionCache.get(request.getSession(false).getId()));

        StringBuilder jsonResponse = new StringBuilder();

        //ObjectMapper sessions = new ObjectMapper();

        //ArrayNode sessionsArray = sessions.createArrayNode();

        if (sessionCache != null && !sessionCache.isEmpty()) {
            sessionCache.cacheEntrySet()
                .stream()
                .forEach(cacheEntry -> {
                    MapSession mapSession = (MapSession) cacheEntry.getValue();

                    log.info("Map Session: {}", mapSession);

                    MovieCart movieCart = mapSession.getAttribute(ShoppingCartController.SESSION_ATTR_MOVIE_CART);

                    log.info("Shopping Cart in Session {} is {}", mapSession.getId(), movieCart);

                    if (movieCart != null) {

                        //ObjectNode movieCartNode = sessions.createObjectNode();
                        //movieCartNode.put("sessionId", mapSession.getId());
                        //movieCartNode.put("orderId", movieCart.getOrderId());
                        jsonResponse.append("sessionId");
                        jsonResponse.append(mapSession.getId());
                        jsonResponse.append(",");
                        jsonResponse.append("orderId");
                        jsonResponse.append(movieCart.getOrderId());
                        jsonResponse.append(",");
                        jsonResponse.append("[");

                        //ArrayNode movieItemsNode = movieCartNode.arrayNode();

                        movieCart.getMovieItems().forEach((movieId, qty) -> {
                            //ObjectNode movieItem = movieItemsNode.addObject();
                            //movieItem.put("movieId", movieId);
                            //movieItem.put("orderQuantity", qty);
                            //movieItemsNode.add(movieItem);
                            jsonResponse.append("movieId:");
                            jsonResponse.append(movieId);
                            jsonResponse.append(",");
                            jsonResponse.append("orderQuantity:");
                            jsonResponse.append(qty);
                            jsonResponse.append(",");
                        });
                        jsonResponse.append("]");
                        //sessionsArray.add(movieCartNode);
                    }
                });
        }


//        try {
//            jsonResponse = sessions.writeValueAsString(sessionsArray);
//        } catch (JsonProcessingException e) {
//            log.error("Error building JSON response for sesisons", e);
//        }

        return jsonResponse.toString();
    }
}
