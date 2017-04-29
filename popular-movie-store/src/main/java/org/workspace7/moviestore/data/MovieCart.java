package org.workspace7.moviestore.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kameshs
 */
@Data
public class MovieCart {
    private String userId;
    private String orderId;
    private Map<String, Integer> movieItems = new HashMap<>();
}
