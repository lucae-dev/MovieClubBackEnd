package com.movies.movie.app.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.movie.app.BackBlaze.BackblazeService;
import com.movies.movie.app.MovieCollection.MovieCollectionDTO;
import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.MovieRating.MovieRatingService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public MovieRatingService movieRatingService;

    @Autowired private BackblazeService backblazeService;


    @GetMapping(path = "/checkToken")
    public boolean checkToken(@AuthenticationPrincipal User user){
        if(user != null){
            return true;
        }
        return false;
    }

    @PostMapping(path = "/registerDeviceToken")
    public String registerDeviceToken(@AuthenticationPrincipal User user, @RequestParam String deviceToken){
        System.out.println("saving Token");
        return userService.registerUserToken(user.getId(), deviceToken);
    }

    @PostMapping(path = "/removeDeviceToken")
    public void removeDeviceToken(@AuthenticationPrincipal User user, @RequestParam String deviceToken){
        System.out.println("removing Token");
        userService.removeUserToken(user.getId(), deviceToken);
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

    @GetMapping("/getUploadUrl")
    public ResponseEntity<String> getUploadUrl(
            @AuthenticationPrincipal User user
    ){
        try {
          return backblazeService.getUploadUrl();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/uploadProPic")
    public ResponseEntity<String> saveProPic(@AuthenticationPrincipal User user, @RequestParam String fileName, @RequestParam String fileId) {
        return userService.uploadProPic(user.getId(), fileName, fileId);
    }

    /*@GetMapping("/savedMovies")
    public Set<MovieRating> getSavedMovies(@RequestParam(required = true) Long user_id){
       // return userService.getSavedMovies(user_id);
        return
    }*/
}
