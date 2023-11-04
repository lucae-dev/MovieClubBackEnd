package com.movies.movie.app.movie;


import com.movies.movie.app.MovieRating.MovieRatingService;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;
import com.movies.movie.app.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(path = "/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRatingService movieRatingService;

    @GetMapping("/authTest")
    public String testAuth(){
        return "Fuck ";
    }
/*
    @GetMapping("/discoverMovies")
    public List<Movie> discoverMovies(@RequestParam Integer genreId, @RequestParam Integer providerId){
        return movieService.discoverMovies(genreId, providerId);
    }*/

    @PostMapping("/areMoviesLiked")
    public List<MovieDTO> areMoviesLiked(@AuthenticationPrincipal User user, @RequestBody List<MovieDTO> movies){
        return movieService.addLikedToDTOList(user,movies);
    }

    @GetMapping("/discoverMovies")
    public List<MovieDTO> discoverMovies(@AuthenticationPrincipal User user,@RequestParam Integer genreId, @RequestParam String providerIds){
        return movieService.discoverMovies(user,genreId, providerIds);
    }
    @GetMapping("/searchMovies")
    public List<MovieDTO> searchMovies(@AuthenticationPrincipal User user,@RequestParam String keyword, @RequestParam  String primaryReleaseYear,
                                      @RequestParam String region,
                                      @RequestParam String language){
        return movieService.searchMovies(user,keyword, primaryReleaseYear,UriUtils.decode(region, "UTF-8"),UriUtils.decode(language, "UTF-8"));
    }

    @GetMapping("/movie/providers")
    public WatchProvidersContainer getProvidersByCountry(@RequestParam Long movieId, @RequestParam String country){
        return  movieService.getProvidersByCountry(movieId, UriUtils.decode(country, "UTF-8"));
    }

    @GetMapping("/movie")
    public MovieDTO getMovieDetails(@AuthenticationPrincipal User user, @RequestParam Long movieId, @RequestParam String language){
        return movieService.getMovieDetailsDTO(user, movieId, UriUtils.decode(language, "UTF-8"));
    }

    @GetMapping("/movie/getRecommendations")
    public List<MovieDTO> getMovieRecommendations(@AuthenticationPrincipal User user, @RequestParam Long movieId, @RequestParam String language, @RequestParam int page){
        return movieService.getMovieRecommendations(user,movieId, UriUtils.decode(language, "UTF-8"), page);
    }

    @GetMapping("/getTrending")
    public List<Movie> getTrending(){
        return movieService.getTrendingMovies();
    }



    @GetMapping("/getAll")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll()/*.sort()*/;
    }

    @PostMapping("/add")
    public Movie addMovie(@Valid @RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    /*
    @PostMapping(value = "/like")
    public void likeMovie(@AuthenticationPrincipal User user, @RequestParam Long movieId){
        //user va preso con
        //var user = repository.findByUsername(jwtService.extractUsername(token.getToken())).orElseThrow();
        //non serve
        movieRatingService.likeMovie(user,movieId);
    }*/

    @PutMapping(value = "/rate")
    public void rateMovie(@AuthenticationPrincipal User user, @RequestParam Long movieId, @RequestParam float rate){
        movieRatingService.rate(user,movieId,rate);
    }

    @PutMapping(value = "/comment")
    public void commentMovie(@AuthenticationPrincipal User user, @RequestParam Long movieId, @RequestParam String comment){
        movieRatingService.comment(user,movieId,comment);
    }

    /*
    @PutMapping(value = "/markSeen")
    public void markSeenMovie(@AuthenticationPrincipal User user, @RequestParam Long movieId){
        movieRatingService.markSeen(user,movieId);
    }*/


}
