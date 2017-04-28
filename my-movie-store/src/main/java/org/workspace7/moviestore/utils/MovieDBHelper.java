package org.workspace7.moviestore.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.AdvancedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.workspace7.moviestore.config.MovieStoreProps;
import org.workspace7.moviestore.data.Movie;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kameshs
 */
@Component
@Slf4j
public class MovieDBHelper {

    public static final String POPULAR_MOVIES_CACHE = "infinispan-moviestore.xml";

    final RestTemplate restTemplate;

    final MovieStoreProps movieStoreProps;

    final AdvancedCache<Long, Movie> moviesCache;

    @Autowired
    public MovieDBHelper(RestTemplate restTemplate, MovieStoreProps movieStoreProps,
                         CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.movieStoreProps = movieStoreProps;
        this.moviesCache = (AdvancedCache<Long, Movie>) cacheManager
            .getCache(POPULAR_MOVIES_CACHE).getNativeCache();
    }

    /**
     * This method queries the external API and caches the movies, for the demo purpose we just query only first page
     *
     * @return - the status code of the invocation
     */
    public int queryAndCache() {

        if (this.moviesCache.isEmpty()) {

            log.info("No movies exist in cache, loading cache ..");

            UriComponentsBuilder moviesUri = UriComponentsBuilder
                .fromUriString(movieStoreProps.getApiEndpointUrl() + "/movie/popular")
                .queryParam("api_key", movieStoreProps.getApiKey());

            final URI requestUri = moviesUri.build().toUri();

            log.info("Request URI:{}", requestUri);

            ResponseEntity<String> response = restTemplate.getForEntity(requestUri, String.class);

            log.info("Response Status:{}", response.getStatusCode());

            Map<Long, Movie> movieMap = new HashMap<>();

            if (200 == response.getStatusCode().value()) {
                String jsonBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode root = objectMapper.readTree(jsonBody);
                    JsonNode results = root.path("results");
                    results.elements().forEachRemaining(movieNode -> {
                        Long id = movieNode.get("id").asLong();
                        Movie movie = Movie.builder()
                            .id(id)
                            .overview(movieNode.get("overview").asText())
                            .popularity(movieNode.get("popularity").floatValue())
                            .posterPath("http://image.tmdb.org/t/p/w300" + movieNode.get("poster_path").asText())
                            .title(movieNode.get("title").asText())
                            .build();
                        movieMap.put(id, movie);
                    });
                } catch (IOException e) {
                    log.error("Error reading response:", e);
                }

                log.debug("Got {} movies", movieMap);
                moviesCache.putAll(movieMap);
            }
            return response.getStatusCode().value();
        } else {
            log.info("Cache already loaded with movies ... will use cache");
            return 200;
        }
    }

    public void query(long movieId) {
        //TODO
    }

//    /**
//     * Computing the MD5 hash based on api public and private key
//     *
//     * @param timestamp - the timestamp to be added to the hash
//     * @return - the MD5 digest of the timestamp + public key + private key
//     * @throws NoSuchAlgorithmException - if the specified algorithm is not present
//     */
//    protected String hash(String timestamp) throws NoSuchAlgorithmException {
//        String hashInput = timestamp + movieStoreProps.getApiKey()
//            + movieStoreProps.getMarvelApiPublicKey();
//        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
//        md5Digest.update(hashInput.toString().getBytes());
//        byte[] md5bytes = md5Digest.digest();
//        String md5Hash = DatatypeConverter.printHexBinary(md5bytes);
//        return md5Hash;
//    }
}
