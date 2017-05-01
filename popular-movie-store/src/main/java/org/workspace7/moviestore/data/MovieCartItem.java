package org.workspace7.moviestore.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kameshs
 */
@Data
@Builder
public class MovieCartItem implements Serializable {
    private Movie movie;
    private int quantity;
    private double total;
}
