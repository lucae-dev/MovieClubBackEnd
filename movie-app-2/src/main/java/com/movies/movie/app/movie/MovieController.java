package com.movies.movie.app.movie;


import com.movies.movie.app.MovieRating.MovieRatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieRatingService movieRatingService;

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
