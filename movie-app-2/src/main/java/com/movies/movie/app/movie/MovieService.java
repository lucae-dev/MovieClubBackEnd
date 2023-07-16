package com.movies.movie.app.movie;

import com.movies.movie.app.TMDB.TMDBService;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.movies.movie.app.WatchProvider.WatchProvider;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> discoverMovies(Integer genreId, Integer providerID){
        List<Movie> movies = tmdbService.discoverMovies(genreId,providerID);
        movieRepository.saveAll(movies);
        return movies;
    }

    public List<Movie> discoverMovies(Integer genreId, String providerIDs){
        List<Movie> movies = tmdbService.discoverMovies(genreId,providerIDs);
        movieRepository.saveAll(movies);
        return movies;
    }
    public List<Movie> getTrendingMovies(){

        List<Movie> movies = tmdbService.getTrendingMovies();

/*
        for (Movie movie : movies){
            movie.setProvider_ids(
                    tmdbService.
                            getMovieWatchProvidersCountry(movie.getId(), "IT").get()
                            .getFlatrate().stream().map(WatchProvider::getProvider_id).toList());
        }
*/
        movieRepository.saveAll(movies);
        return movies;
    }

    public WatchProvidersContainer getProvidersByCountry(Long movieId, String country){
        return tmdbService.getMovieWatchProvidersCountry(movieId, country);
    }




}
