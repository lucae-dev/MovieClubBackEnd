package com.movies.movie.app.user;


import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.MovieRating.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public MovieRatingService movieRatingService;

    /*@GetMapping("/savedMovies")
    public Set<MovieRating> getSavedMovies(@RequestParam(required = true) Long user_id){
       // return userService.getSavedMovies(user_id);
        return
    }*/
}
