package org.workspace7.moviestore.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.workspace7.moviestore.data.Movie;
import org.workspace7.moviestore.data.MovieCart;
import org.workspace7.moviestore.data.MovieCartItem;
import org.workspace7.moviestore.utils.MovieDBHelper;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kameshs
 */
@Controller
@Scope("session")
@Slf4j
@Data
public class ShoppingCartController {

    @Autowired
    MovieDBHelper movieDBHelper;

    private final MovieCart movieCart = new MovieCart();

    @GetMapping("/cart/add")
    public @ResponseBody
    String addItemToCart(@RequestParam("movieId") String movieId, @RequestParam("quantity") int qty,
                         HttpSession session) {
        movieCart.setOrderId(session.getId());

        log.info("Adding/Updating {} with Quantity {} to cart ", movieId, qty);

        Map<String, Integer> movieItems = movieCart.getMovieItems();

        movieItems.put(movieId, qty);

        log.info("Movie Cart:{}", movieCart);

        return String.valueOf(movieCart.getMovieItems().size());
    }

    @GetMapping("/cart/show")
    public String showCart(Map<String, Object> model) {
        log.info("Showing Cart {}", movieCart);
        model.put("movieCart", movieCart);
        Map<String, Integer> movieItems = movieCart.getMovieItems();
        List<MovieCartItem> cartMovies = movieCart.getMovieItems().keySet().stream()
            .map(movieId -> {
                Movie movie = movieDBHelper.query(movieId);
                int quantity = movieItems.get(movieId);
                double total = quantity * movie.getPrice();
                log.info("Movie:{} total for {} items is {}", movie, quantity, total);
                return MovieCartItem.builder()
                    .movie(movie)
                    .quantity(quantity)
                    .total(total)
                    .build();
            })
            .collect(Collectors.toList());
        model.put("cartItems", cartMovies);
        model.put("cartCount", cartMovies.size());
        return "cart";
    }

    @PostMapping("/cart/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public void checkout() {
        log.info("Your request {} will be processed, thank your for shopping", movieCart);
    }
}
