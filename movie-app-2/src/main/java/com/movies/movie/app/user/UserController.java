package com.movies.movie.app.user;


import com.movies.movie.app.MovieCollection.MovieCollectionDTO;
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



    @PostMapping(path = "/registerDeviceToken")
    public String registerDeviceToken(@AuthenticationPrincipal User user, @RequestParam String deviceToken){
        return userService.registerUserToken(user.getId(), deviceToken);
    }

    @PutMapping(path = "/{followedId}/follow")
    public boolean followUser(@AuthenticationPrincipal User followerUser, @PathVariable Long followedId){
        System.out.println("user Controller");

        return userService.followUser(followerUser.getId(),followedId);
    }

    @PutMapping(path = "/{followedId}/unfollow")
    public boolean unfollowUser(@AuthenticationPrincipal User followerUser, @PathVariable Long followedId){
        return userService.unfollowUser(followerUser.getId(),followedId);
    }

    @PostMapping(path = "/setBio")
    public String setDescription(@AuthenticationPrincipal User user,@RequestBody String bio){
        return userService.setDescription(user, bio);
    }
    @GetMapping(path = "/getUserInfo")
    public UserDTO getUser(@AuthenticationPrincipal User user, @RequestParam Long userId){
        return userService.getUserPageInfo(user,userId);
    }

    @GetMapping(path = "/getMyUserInfo")
    public UserDTO getUser(@AuthenticationPrincipal User user){
        return userService.getUserPageInfo(user,user.getId());
    }


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

    @GetMapping("/publicCollections")
    public List<MovieCollectionDTO> getPublicCollections(@RequestParam Long userId) {
        return userService.getPublicCollections(userId);
    }


    /*@GetMapping("/savedMovies")
    public Set<MovieRating> getSavedMovies(@RequestParam(required = true) Long user_id){
       // return userService.getSavedMovies(user_id);
        return
    }*/
}
