package com.movies.movie.app.user;

import com.movies.movie.app.FCMToken.DeviceToken;
import com.movies.movie.app.FCMToken.DeviceTokenRepository;
import com.movies.movie.app.MovieCollection.MovieCollection;
import com.movies.movie.app.MovieCollection.MovieCollectionDTO;
import com.movies.movie.app.MovieCollection.MovieCollectionRepository;
import com.movies.movie.app.MovieCollection.MovieCollectionService;
import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.Notifications.NotificationService;
import com.movies.movie.app.auth.AuthenticationService;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieDTO;
import com.movies.movie.app.movie.MovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    AuthenticationService authenticationService;



    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieCollectionService movieCollectionService;
    @Autowired
    MovieService movieService;

    @Autowired
    DeviceTokenRepository deviceTokenRepository;

    @Autowired
    NotificationService notificationService;



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

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setLocked(user.isLocked());
        userDTO.setVerified(user.isVerified());
       // userDTO.setProviderIds(user.getProviderIds()); // This might need further mapping
        userDTO.setEmail(user.getEmail());
        userDTO.setPropic(user.getPropic());
        userDTO.setBiography(user.getBiography());

        userDTO.setFollowers_count(user.getFollowers_count());
        userDTO.setFollowing_count(user.getFollowing_count());

        userDTO.setSeen(user.getSeen());

        return userDTO;
    }


    public List<UserDTO> addFollowingToDTOList(User user2, List<UserDTO> userDTOS){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        for(UserDTO userDTO:userDTOS){
            if(user.getFollowing().stream().anyMatch(userFollowed -> Objects.equals(userFollowed.getId(),userDTO.getId()))){
                userDTO.setFollowed(Boolean.TRUE);
            }
            else {
                userDTO.setFollowed(Boolean.FALSE);
            }
        }
        return userDTOS;
    }


    public UserDTO addFollowedToDTO(User user2, UserDTO userDTO){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));

            if(user.getFollowing().stream().anyMatch(userFollower -> Objects.equals(userFollower.getId(),userDTO.getId()))){
                userDTO.setFollowed(Boolean.TRUE);
            }
            else {
                userDTO.setFollowed(Boolean.FALSE);
            }

        return userDTO;
    }

    public List<UserDTO> addFollowerToDTOList(User user2, List<UserDTO> userDTOS){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        for(UserDTO userDTO:userDTOS){
            if(user.getFollowers().stream().anyMatch(userFollower -> Objects.equals(userFollower.getId(),userDTO.getId()))){
                userDTO.setFollower(Boolean.TRUE);
            }
            else {
                userDTO.setFollower(Boolean.FALSE);
            }
        }
        return userDTOS;
    }

    public List<UserDTO> addFollowerAndFollowingToDTOList(User user2, List<UserDTO> userDTOS){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        for(UserDTO userDTO:userDTOS){
            if(user.getFollowers().stream().anyMatch(userFollower -> Objects.equals(userFollower.getId(),userDTO.getId()))){
                userDTO.setFollower(Boolean.TRUE);
            }
            else {
                userDTO.setFollower(Boolean.FALSE);
            }
            if(user.getFollowing().stream().anyMatch(userFollowed -> Objects.equals(userFollowed.getId(),userDTO.getId()))){
                userDTO.setFollowed(Boolean.TRUE);
            }
            else {
                userDTO.setFollowed(Boolean.FALSE);
            }
        }
        return userDTOS;
    }


    public List<UserDTO> searchByKeyword(User user,String keyword, Pageable pageable) {
        Page<User> users = userRepository.searchByKeyword(keyword, pageable);
        List<UserDTO> userDTOs = users.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return addFollowerAndFollowingToDTOList(user, userDTOs);
    }
    public boolean followUser(Long followerId, Long followingId) {
        // Load the users from the database
        User follower = userRepository.findById(followerId).orElseThrow(() -> new IllegalStateException("User not found"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user is already following the other user
        if (follower.getFollowing().contains(following)) {
            return true; // Already following
        }

        // Modify both sides of the relationship
        follower.getFollowing().add(following);
        following.getFollowers().add(follower);

        // Update the follower and following counters
        follower.setFollowing_count(follower.getFollowing_count() + 1);
        following.setFollowers_count(following.getFollowers_count() + 1);

        // Save the changes to the database
        userRepository.save(follower);
        userRepository.save(following);
        notificationService.followNotification(follower, following);

        return true; // Successfully followed
    }

    public boolean unfollowUser(Long followerId, Long followingId) {
        // Load the users from the database
        User follower = userRepository.findById(followerId).orElseThrow(() -> new IllegalStateException("User not found"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user is not following the other user
        if (!follower.getFollowing().contains(following)) {
            return false; // Not following
        }

        // Modify both sides of the relationship
        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);

        // Update the follower and following counters
        follower.setFollowing_count(follower.getFollowing_count() - 1);
        following.setFollowers_count(following.getFollowers_count() - 1);

        // Save the changes to the database
        userRepository.save(follower);
        userRepository.save(following);

        return false; // Successfully unfollowed
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

    public List<MovieCollectionDTO> getPublicCollections(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User not found"));

        return movieCollectionService.convertListToDTO(user.getMyCollections().stream().filter(movieCollection -> movieCollection.isVisible()).toList());

    }



    public UserDTO getUserPageInfo(User userMain2, Long userId) {
        User user = userRepository.findUserWithVisibleCollections(userId).orElseThrow(()->new IllegalStateException("User not found"));
        UserDTO userDTO = convertToDTO(user);

        userDTO.setMyCollections(movieCollectionService.convertListToDTO( user.getMyCollections()));
        final UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(userDTO.getId());
        userDTO1.setUsername(userDTO.getUsername());
        for(MovieCollectionDTO movieCollectionDTO: userDTO.getMyCollections()){
            movieCollectionDTO.setOwner(userDTO1);
        }
        List<Movie> seenMovies = user.getSeenCollection().getMovies().subList(0,Integer.min( user.getSeenCollection().getMovies().size(), 9));
        if(!seenMovies.isEmpty()) {
            MovieCollectionDTO movieCollectionDTO =movieCollectionService.convertToDTO(user.getSeenCollection());
                  movieCollectionDTO.setMovies(movieService.addLikedToDTOList(userMain2, movieService.convertListToDTO(movieService.addProvidersToList(seenMovies))));
            userDTO.setSeenCollection(movieCollectionDTO);
        }
        return addFollowedToDTO( userMain2, userDTO);
    }



    public String registerUserToken(Long userId, String deviceTokenStr){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setToken(deviceTokenStr);
        deviceToken.setUser(user);
        deviceTokenRepository.save(deviceToken);
        return deviceToken.getToken();
    }

//


}
