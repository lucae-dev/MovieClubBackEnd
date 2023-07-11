package com.movies.movie.app.movie;


import com.movies.movie.app.MovieRating.MovieRatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/discoverMovies")
    public List<Movie> discoverMovies(@RequestParam Integer genreId, @RequestParam Integer providerId){
        return movieService.discoverMovies(genreId, providerId);
    }

    @GetMapping("/getAll")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll()/*.sort()*/;
    }

    @PostMapping("/add")
    public Movie addMovie(@Valid @RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    @PostMapping(value = "/like")
    public void likeMovie(@RequestParam Long userId, @RequestParam Long movieId){
        //user va preso con
        //var user = repository.findByUsername(jwtService.extractUsername(token.getToken())).orElseThrow();
        //non serve
        movieRatingService.addMovie(userId,movieId);
    }

    @PutMapping(value = "/rate")
    public void rateMovie(@RequestParam Long userId, @RequestParam Long movieId, @RequestParam float rate){
        movieRatingService.rate(userId,movieId,rate);
    }

    @PutMapping(value = "/comment")
    public void commentMovie(@RequestParam Long userId, @RequestParam Long movieId, @RequestParam String comment){
        movieRatingService.comment(userId,movieId,comment);
    }

    @PutMapping(value = "/markSeen")
    public void markSeenMovie(@RequestParam Long userId, @RequestParam Long movieId){
        movieRatingService.markSeen(userId,movieId);
    }


}
