package com.movies.movie.app.movie;

import com.movies.movie.app.TMDB.TMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private TMDBService tmdbService;

    public List<Movie> discoverMovies(Integer genreId, Integer providerID){
        return tmdbService.discoverMovies(genreId, providerID);
    }

}
