package org.workspace7.moviestore.data;

import lombok.Builder;
import lombok.Data;

/**
 * @author kameshs
 */
@Data
@Builder
public class MovieCartItem {
    private Movie movie;
    private int quantity;
    private double total;
}
