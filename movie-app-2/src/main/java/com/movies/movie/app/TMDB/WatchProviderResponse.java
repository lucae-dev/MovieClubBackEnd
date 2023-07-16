package com.movies.movie.app.TMDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchProviderResponse {

    private int movieId;
    private Map<String, WatchProvidersContainer> results;

    public WatchProviderResponse() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Map<String, WatchProvidersContainer> getResults() {
        return results;
    }

    public void setResults(Map<String, WatchProvidersContainer> results) {
        this.results = results;
    }
}
