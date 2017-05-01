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
import org.springframework.web.servlet.ModelAndView;
import org.workspace7.moviestore.data.Movie;
import org.workspace7.moviestore.data.MovieCart;
import org.workspace7.moviestore.data.MovieCartItem;
import org.workspace7.moviestore.utils.MovieDBHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ModelAndView home(ModelAndView modelAndView, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        List<Movie> movies = movieDBHelper.getAll();

        List<MovieCartItem> movieList = movies.stream()
            .map((Movie movie) -> MovieCartItem.builder()
                .movie(movie)
                .quantity(0)
                .total(0)
                .build())
            .collect(Collectors.toList());

        if (session != null) {
            AdvancedCache<String, Object> sessionCache = (AdvancedCache<String, Object>)
                cacheManager.getCache("moviestore-sessions-cache").getNativeCache();

            Optional<MapSession> mapSession = Optional.ofNullable((MapSession) sessionCache.get(session.getId()));

            log.info("Session already exists, retrieving values from session {}", mapSession);

            int cartCount = 0;

            if (mapSession.isPresent()) {

                MovieCart movieCart = mapSession.get().getAttribute(ShoppingCartController.SESSION_ATTR_MOVIE_CART);

                if (movieCart != null) {

                    log.info("Movie Cart:{} for session id:{}", movieCart);

                    final Map<String, Integer> movieItems = movieCart.getMovieItems();

                    movieList = movieList.stream()
                        .map(movieCartItem -> {
                            Movie movie = movieCartItem.getMovie();
                            String movieId = movie.getId();
                            if (movieItems.containsKey(movieId)) {
                                int quantity = movieItems.get(movieId);
                                movieCartItem.setQuantity(quantity);
                            }
                            return movieCartItem;

                        }).collect(Collectors.toList());

                    cartCount = movieItems.size();
                }
            }
            modelAndView.addObject("cartCount", cartCount);
            modelAndView.addObject("movies", movieList);
        } else {
            log.info("New Session");
            modelAndView.addObject("movies", movieList);
        }
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/healthz")
    @ResponseStatus(HttpStatus.OK)
    public void healthz(Map<String, Object> model) {
        log.trace("Health check seems to be good...");
    }

}
