package com.movies.movie.app.user;


import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.MovieRating.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public MovieRatingService movieRatingService;

    @PostMapping(path = "/setProviders")
    public List<Integer> setProviderIds(@AuthenticationPrincipal User user, @RequestBody List<Integer> providerIds){
        return userService.setProviders(user,providerIds);
    }

    @GetMapping(path = "/getProviders")
    public List<Integer> getProviderIds(@AuthenticationPrincipal User user){

        return userService.getProviders(user);
    }

    @GetMapping(path="/getLikedId")
    public List<Long>  getLikedIds(@AuthenticationPrincipal User user){
        return userService.getLikedIds(user);
    }

    /*@GetMapping("/savedMovies")
    public Set<MovieRating> getSavedMovies(@RequestParam(required = true) Long user_id){
       // return userService.getSavedMovies(user_id);
        return
    }*/
}
