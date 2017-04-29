package org.workspace7.moviestore.data;

import lombok.Builder;
import lombok.Data;

/**
 * @author kameshs
 */
@Data
@Builder
public class Movie {
    private String id;
    private String posterPath;
    private String overview;
    private String title;
    private float popularity;
    private double price;
}
