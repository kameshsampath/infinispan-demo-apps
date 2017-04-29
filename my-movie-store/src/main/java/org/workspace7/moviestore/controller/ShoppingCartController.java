package org.workspace7.moviestore.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.workspace7.moviestore.data.Movie;
import org.workspace7.moviestore.data.MovieCart;
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
        List<Movie> cartMovies = movieCart.getMovieItems().keySet().stream()
            .map(movieDBHelper::query)
            .collect(Collectors.toList());
        model.put("cartItems", movieCart.getMovieItems());
        model.put("cartCount", cartMovies.size());
        return "cart";
    }

    @GetMapping("/cart/checkout")
    public void checkout() {
        log.info("Checking out the cart {}", movieCart);
    }
}
