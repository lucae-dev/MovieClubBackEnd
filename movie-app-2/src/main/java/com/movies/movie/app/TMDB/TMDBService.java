package com.movies.movie.app.TMDB;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.movies.movie.app.MovieRating.MovieRatingService;
import com.movies.movie.app.People.Person;
import com.movies.movie.app.WatchProvider.WatchProvider;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;
import com.movies.movie.app.movie.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
        import org.springframework.web.reactive.function.client.WebClient;
        import reactor.core.publisher.Mono;


@Service
public class TMDBService {
    private final String apiKey = "e80f5ca655662e773cd9e67c26cb8c72"; // replace with your own API Key
    private final String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5ZWU2ZjA0YjdhNmZiYzk2ZjdkNjE3MjdjZjc5ZGRiOSIsInN1YiI6IjYzZjdkODY0ZDFjYTJhMDBhYWQ4Mzg2OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.YGgc4gXk18N_pMKPiRpyBgbl0YEtqgjqWvhwwDCtmUY";
    private final RestTemplate restTemplate;

    @Autowired
    public TMDBService() {
        this.restTemplate = new RestTemplate();
    }

    /*
    public List<Movie> discoverMovies(Integer genreId, Integer providerId) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_genres", genreId)
                .queryParam("watch_region", "IT")
                .queryParam("with_watch_providers", providerId)
                .toUriString();

        ResponseEntity<MovieListResponse> responseEntity = restTemplate.getForEntity(uri, MovieListResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }*/





    public List<Movie> searchMovies( String keyword, String primaryReleaseYear, String Region, String language) {
        System.out.println(keyword);

        String uri;
       if(!primaryReleaseYear.isBlank()) {
            uri = UriComponentsBuilder
                   .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                   .queryParam("api_key", apiKey)
                    .queryParam("query", keyword)
                    .queryParam("include_adult", "false")
                   .queryParam("primary_release_year", primaryReleaseYear)
                   .queryParam("language", language)
                   .queryParam("watch_region", Region)
                    .build(false)
                   .toUriString();
       }
       else{
            uri = UriComponentsBuilder
                   .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                   .queryParam("api_key", apiKey)
                    .queryParam("query", keyword)
                    .queryParam("include_adult", "false")
                   .queryParam("language", language)
                   .queryParam("watch_region", Region)
                    .build(false)
                   .toUriString();
       }
        System.out.println(uri);

        ResponseEntity<MovieListResponse> responseEntity = restTemplate.getForEntity(uri, MovieListResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults();
        } else {
            throw new RuntimeException("Failed to search movies");
        }
    }



    public List<Movie> discoverMovies(Integer genreId, String providerIds) {
        final String watch_region = "IT";
        final String language = "it-IT";
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_genres", genreId)
                .queryParam("language", language)
                .queryParam("watch_region", watch_region)
                .toUriString();
        uri +="&with_watch_providers=" + providerIds;
        System.out.println(uri);
        ResponseEntity<MovieListResponse> responseEntity = restTemplate.getForEntity(uri, MovieListResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }


    public List<Movie> getTrendingMovies() {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/trending/movie/day")
                .queryParam("api_key", apiKey)
                .queryParam("language", "it-IT")
                .toUriString();

        ResponseEntity<MovieListResponse> responseEntity = restTemplate.getForEntity(uri, MovieListResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }

    public Movie getMovieDetails(Long movieId, String language){
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/"+ movieId )
                .queryParam("api_key", apiKey)
                .queryParam("language", language)
                .toUriString();

        System.out.println(uri);
        ResponseEntity<Movie> responseEntity = restTemplate.getForEntity(uri, Movie.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }


    public List<WatchProvidersContainer> getMovieWatchProvidersAll(Long movieId) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/"+ movieId +"/watch/providers")
                .queryParam("api_key", apiKey)
                .toUriString();

        ResponseEntity<WatchProviderResponse> responseEntity = restTemplate.getForEntity(uri, WatchProviderResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults().values().stream().toList();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }

    public List<Movie> getMovieRecommendations(Long movieId, String language, int page) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/"+ movieId +"/recommendations")
                .queryParam("api_key", apiKey)
                .queryParam("language", language)
                .queryParam("page", page)
                .toUriString();

        ResponseEntity<MovieListResponse> responseEntity = restTemplate.getForEntity(uri, MovieListResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().getResults();
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }

    public WatchProvidersContainer getMovieWatchProvidersCountry(Long movieId, String country) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/movie/"+ movieId +"/watch/providers")
                .queryParam("api_key", apiKey)
                .toUriString();

        ResponseEntity<WatchProviderResponse> responseEntity = restTemplate.getForEntity(uri, WatchProviderResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            Map<String, WatchProvidersContainer> results = responseEntity.getBody().getResults();
            if(results.containsKey(country)) {
                return results.get(country);
            } else {
                //("Country " + country + " not found in the results map");
                return null;
            }
        } else {
            throw new RuntimeException("Failed to fetch movies");
        }
    }


}






        /*


@Service
public class TMDBService {
    private final String apiKey = "9ee6f04b7a6fbc96f7d61727cf79ddb9"; // replace with your own API Key
    private final WebClient webClient;

    @Autowired
    public TMDBService() {
        this.webClient = WebClient.builder().baseUrl("https://api.themoviedb.org/3").build();
    }

    public Mono<List<Movie>> discoverMovies(Integer genreId, Integer providerId) {

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("with_genres", genreId)
                        .queryParam("watch_region", "IT")
                        .queryParam("with_watch_providers", providerId)
                        .build())
                .retrieve()
                .bodyToMono(MovieListResponse.class)
                .map(MovieListResponse::getResults);
    }
}

*/



/*
public class TMDBService {

    private final String apiKey = "9ee6f04b7a6fbc96f7d61727cf79ddb9";
    private final String baseUrl = "https://api.themoviedb.org/3/";
    private RestTemplate restTemplate;

    public TMDBService() {
        restTemplate = new RestTemplate();
    }

    // Function to search for movies, TV shows, or people by a query string, it returns both movies, TV shows and people
   /* To be used first you have to handle TVshows, people and create a class composed by a list of each, so you can retrieve results
    public MultiSearchResults search(String query) {
        String url = String.format(baseUrl + "search/multi?query=%s&api_key=%s", query, apiKey);
        return restTemplate.getForObject(url, MultiSearchResults.class);
    }
*/


    // Function to search for TV shows by a query string
/* Handle TVShow to use!!! It can just return a List<TVShows>
 public TVSearchResults searchTVShows(String query) {
        String url = String.format(baseUrl + "search/tv?query=%s&api_key=%s", query, apiKey);
        return restTemplate.getForObject(url, TVSearchResults.class);
    }
*/
/*
    // Function to search for movies by a query string, result in the countrycode language
    public List<Movie> searchMovies(String query, String countrycode) {
        String url = baseUrl + "/search/movie?api_key=" + apiKey + "&query=" + query + "&language=" + countrycode;
        ResponseEntity<MovieListResponse> response = restTemplate.getForEntity(url, MovieListResponse.class);
        List<Movie> movies = response.getBody().getResults();

        for (Movie movie : movies){
            movie.setLanguage(countrycode);
        }
        return movies;

    }


    // Function to search for directors, actors, writers or producers by a query string
    /*
    public List<Person> searchPeople(String query) {
        String url = String.format(baseUrl + "search/multi?query=%s&api_key=%s&include_adult=false&with_crew=22,23,24,25", query, apiKey);
        ParameterizedTypeReference<List<LinkedHashMap<String, Object>>> responseType = new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {};
        List<LinkedHashMap<String, Object>> results = restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
        List<Person> people = new ArrayList<>();
        for (LinkedHashMap<String, Object> result : results) {
            String mediaType = (String) result.get("media_type");
            if (mediaType.equals("person")) {
                Person person = new Person();
                person.setId((Long) result.get("id"));
                person.setName((String) result.get("name"));
                person.setProfilePath((String) result.get("profile_path"));
                people.add(person);
            }
        }
        return people;
    }*/


    // Function to get movie or TV show details by ID
    /* When you implkement also tvshows
    public MultiDetails getDetails(Long id) {
        String url = String.format(baseUrl + "multi/%s?api_key=%s", id, apiKey);
        return restTemplate.getForObject(url, MultiDetails.class);
    }*/
/*
    // Method to retrieve the details of a movie with the given ID
    public List<Movie> getMovieDetails(int movieId, String languagecode) {
        String url = baseUrl + "movie/" + movieId + "?api_key=" + apiKey + "&language=" + languagecode;
        ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);

        Movie movie = response.getBody();
        return movie;
    }


    // Method to retrieve the details of a TV show with the given ID
    /*
    public TVShowDetails getTVShowDetails(int tvShowId) {
        String url = BASE_URL + "tv/" + tvShowId + "?api_key=" + API_KEY;
        return restTemplate.getForObject(url, TVShowDetails.class);
    }*/


/*
    // Function to get streaming services for a movie or TV show by ID
    //country codes: IT, FR, DE, ES, US, CA, GB, FR, JP, KR, AU
    public List<WatchProvider> getMovieProviders(int movieId, String countryCode) {
        String url = String.format(baseUrl + "movie/%d/watch/providers?api_key=%s", movieId, apiKey);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> providersMap = response.getBody();

        List<WatchProvider> providers = new ArrayList<>();
        Map<String, Object> resultsMap = (Map<String, Object>) providersMap.get("results");
        if (resultsMap != null) {
            Map<String, Object> countryMap = (Map<String, Object>) resultsMap.get(countryCode);
            if (countryMap != null) {
                List<Object> buyList = (List<Object>) countryMap.get("buy");
                if (buyList != null) {
                    for (Object obj : buyList) {
                        Map<String, Object> providerMap = (Map<String, Object>) obj;
                        providers.add(new WatchProvider(
                                (String) providerMap.get("provider_name"),
                                (int) providerMap.get("provider_id"),
                                (String) providerMap.get("logo_path"),
                                (String) providerMap.get("url"),
                                "buy"
                        ));
                    }
                }

                List<Object> rentList = (List<Object>) countryMap.get("rent");
                if (rentList != null) {
                    for (Object obj : rentList) {
                        Map<String, Object> providerMap = (Map<String, Object>) obj;
                        providers.add(new WatchProvider(
                                (String) providerMap.get("provider_name"),
                                (int) providerMap.get("provider_id"),
                                (String) providerMap.get("logo_path"),
                                (String) providerMap.get("url"),
                                "rent"
                        ));
                    }
                }

                List<Object> flatrateList = (List<Object>) countryMap.get("flatrate");
                if (flatrateList != null) {
                    for (Object obj : flatrateList) {
                        Map<String, Object> providerMap = (Map<String, Object>) obj;
                        providers.add(new WatchProvider(
                                (String) providerMap.get("provider_name"),
                                (int) providerMap.get("provider_id"),
                                (String) providerMap.get("logo_path"),
                                (String) providerMap.get("url"),
                                "subscription"
                        ));
                    }
                }
            }
        }

        return providers;
    }


}
*/