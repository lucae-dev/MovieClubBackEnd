package com.movies.movie.app.user;

import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.auth.AuthenticationService;
import com.movies.movie.app.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;




/*
    public Set<MovieRating> getRatedMovies(Long userId){
        return  userRepository.findById(userId).get().getRatedMovies();
    }

    public UserDetails getUserFromToken () throws Exception {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
            UserDetails userDetails = (UserDetails) authToken.getPrincipal();
            return userDetails;
        } else {
            throw new Exception("No user has logged in");
        }
    }
*/

    public boolean follow(User user2, Long followed_id){
       User followed_user = userRepository.findById(followed_id).orElseThrow(()-> new IllegalStateException("User not found"));
       User follower_user = userRepository.findById(user2.getId()).orElseThrow(()-> new IllegalStateException("User not found"));

       followed_user.getFollowers().add(follower_user);
       userRepository.saveAndFlush(followed_user);

       return follower_user.getFollowers().contains(followed_user);

    }


    public String setDescription(User user, String description){
        user.setBiography(description);
        userRepository.save(user);

        return user.getBiography();

    }

    public List<Integer> setProviders(User user, List<Integer> provIds){
        user.setProviderIds(provIds);
        userRepository.save(user);
        userRepository.flush();
        return user.getProviderIds();
    }

    public List<Integer> getProviders(User user) {
        return user.getProviderIds();
    }

    public List<Long> getLikedIds(User user2) {
        User user  = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        List<Long> likedIds = new ArrayList<>();
        for(Movie movie: user.getLikedCollection().getMovies()){
            likedIds.add(movie.getId());
        }
        return likedIds;
    }


//


}
