package org.workspace7.moviestore.data;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kameshs
 */
@Data
public class MovieCart implements Serializable {
    private String userId;
    private String orderId;
    private Map<String, Integer> movieItems = new HashMap<>();
}
