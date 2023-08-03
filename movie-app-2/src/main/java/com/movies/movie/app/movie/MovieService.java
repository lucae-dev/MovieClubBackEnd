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




    public List<Movie> discoverMovies(Integer genreId, String providerIDs){
        List<Movie> movies = tmdbService.discoverMovies(genreId,providerIDs);

        for (Movie movie : movies){
            WatchProvidersContainer container = tmdbService.getMovieWatchProvidersCountry(movie.getId(), "IT");
            if (container != null) {
                List<WatchProvider> flatrate = container.getFlatrate();
                if (flatrate != null) {
                    movie.setStreaming_ids(flatrate.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
                List<WatchProvider> rent = container.getRent();
                if (rent != null) {
                    movie.setRent_ids(rent.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
                List<WatchProvider> buy = container.getBuy();
                if (buy != null) {
                    movie.setBuy_ids(buy.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
            } else {
                // Handle the situation where container is null
                System.out.println("No watch provider available for the provided country and movieId");
            }
        }
        movieRepository.saveAll(movies);
        return movies;
    }

    public List<Movie> getTrendingMovies(){

        List<Movie> movies = tmdbService.getTrendingMovies();

        for (Movie movie : movies){
            WatchProvidersContainer container = tmdbService.getMovieWatchProvidersCountry(movie.getId(), "IT");
            if (container != null) {
                List<WatchProvider> flatrate = container.getFlatrate();
                if (flatrate != null) {
                    movie.setStreaming_ids(flatrate.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
                List<WatchProvider> rent = container.getRent();
                if (flatrate != null) {
                    movie.setRent_ids(flatrate.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
                List<WatchProvider> buy = container.getBuy();
                if (flatrate != null) {
                    movie.setBuy_ids(flatrate.stream().map(WatchProvider::getProvider_id).toList());
                } else {
                    // handle the situation where flatrate is null
                    System.out.println("No flatrate available for the provided country and movieId");
                }
            } else {
                // Handle the situation where container is null
                System.out.println("No watch provider available for the provided country and movieId");
            }
        }

        movieRepository.saveAll(movies);
        return movies;
    }

    public WatchProvidersContainer getProvidersByCountry(Long movieId, String country){
        return tmdbService.getMovieWatchProvidersCountry(movieId, country);
    }

    public Movie getMovieDetails(Long movieId, String language){
        return tmdbService.getMovieDetails(movieId, language);
    }

    public List<Movie> getMovieRecommendations(Long movieId, String language, int page){
        return tmdbService.getMovieRecommendations(movieId, language, page);
    }



}
