package com.movies.movie.app.movie;

import com.movies.movie.app.Genre.Genre;
import com.movies.movie.app.TMDB.TMDBService;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.movies.movie.app.WatchProvider.WatchProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MovieService {
    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;


    public MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setMovie_id(movie.getMovie_id());
        movieDTO.setId(movie.getId());
        movieDTO.setOriginal_language(movie.getOriginal_language());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setRelease_date(movie.getRelease_date());
        movieDTO.setGenres(movie.getGenres());
        movieDTO.setGenre_ids(movie.getGenre_ids());
        movieDTO.setStreaming_ids(movie.getStreaming_ids());
        movieDTO.setRent_ids(movie.getRent_ids());
        movieDTO.setBuy_ids(movie.getBuy_ids());
        movieDTO.setOverview(movie.getOverview());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setTotpoints(movie.getTotpoints());
        movieDTO.setPopularity(movie.getPopularity());
        movieDTO.setVote_average(movie.getVote_average());
        movieDTO.setVote_count(movie.getVote_count());
        movieDTO.setPoster_path(movie.getPoster_path());
        movieDTO.setBackdrop_path(movie.getBackdrop_path());
        movieDTO.setVotes(movie.getVotes());
        movieDTO.setRuntime(movie.getRuntime());
        movieDTO.setBudget(movie.getBudget());
        movieDTO.setRevenue(movie.getRevenue());
        movieDTO.setTagline(movie.getTagline());

        if(movieDTO.getGenre_ids()==null || movieDTO.getGenre_ids().isEmpty()){
            List<Integer> genresId = new ArrayList<>();
            if(movieDTO.getGenres()!=null){
            for(Genre genre: movieDTO.getGenres()){
               genresId.add(genre.getId());
            }
            movieDTO.setGenre_ids(genresId);
            }
        }

        return movieDTO;
    }



    public List<MovieDTO> convertListToDTO(List<Movie> movies){
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for(Movie movie:movies){
            MovieDTO movieDTOtemp = convertToDTO(movie);
            movieDTOS.add(movieDTOtemp);
        }
        return movieDTOS;
    }

    public Movie addProviders(Movie movie){
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
        return movie;
    }

    public List<Movie> addProvidersToList(List<Movie> movies){
        for(Movie m:movies){
            addProviders(m);
            System.out.println(m.getStreaming_ids());
        }
        return movies;
    }

    public MovieDTO addLikedToDTO(User user2, MovieDTO movieDTO){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        if(user.getLikedCollection().getMovies().stream().anyMatch(movieLiked -> Objects.equals(movieLiked.getId(),movieDTO.getId()))){
            movieDTO.setLiked(Boolean.TRUE);
        }
        else {
            movieDTO.setLiked(Boolean.FALSE);
        }
        return movieDTO;
    }
    public List<MovieDTO> addLikedToDTOList(User user2, List<MovieDTO> movieDTOS){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        for(MovieDTO movieDTO:movieDTOS){
            if(user.getLikedCollection().getMovies().stream().anyMatch(movieLiked -> Objects.equals(movieLiked.getId(),movieDTO.getId()))){
                movieDTO.setLiked(Boolean.TRUE);
            }
            else {
                movieDTO.setLiked(Boolean.FALSE);
            }
        }
        return movieDTOS;
    }

    public List<MovieDTO> searchMovies(User user,String keyword, String primaryReleaseYear,
                                        String region,
                                        String language){

        List<Movie> movies = tmdbService.searchMovies(keyword, primaryReleaseYear,region,language);
        movies = addProvidersToList(movies);
        movieRepository.saveAll(movies);
        return addLikedToDTOList(user,convertListToDTO(movies));
    }


    public List<MovieDTO> discoverMovies(User user, Integer genreId, String providerIDs){

        List<Movie> movies = tmdbService.discoverMovies(genreId,providerIDs);
        movies = addProvidersToList(movies);
        movieRepository.saveAll(movies);
        return addLikedToDTOList(user,convertListToDTO(movies));
    }
/*
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
*/
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
        return  tmdbService.getMovieDetails(movieId, language);
    }

    public MovieDTO getMovieDetailsDTO(User user,Long movieId, String language){
        return addLikedToDTO(user, convertToDTO(addProviders(tmdbService.getMovieDetails(movieId, language))));
    }

    public List<MovieDTO> getMovieRecommendations(User user, Long movieId, String language, int page){
        return  addLikedToDTOList(user, convertListToDTO(addProvidersToList(tmdbService.getMovieRecommendations(movieId, language, page))));
    }



}
