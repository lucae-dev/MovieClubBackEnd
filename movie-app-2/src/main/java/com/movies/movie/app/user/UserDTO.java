package com.movies.movie.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.MovieCollection.MovieCollection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDTO {



    private Long id;
    private String username;
    private String password;
    private Role role;
    private boolean locked = false;
    private boolean verified =  false;
    private List<Integer> providerIds;
    // birthday !! Important for recommendations
    private String email;
    private String propic;
    //Firebase token for notifications !!!!!!!
   /* @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<MovieRating> movieRatings;
*/
    //@OneToMany(mappedBy = )
    //movie comments


    private Set<User> followers;


    private Set<User> following;

    //incremented by triggers in database whenever a user gets a new follower
    private int followers_count;
    //incremented by triggers in database whenever a user starts to follow another one
    private int following_count;


    private MovieCollection seenCollection;

    private MovieCollection toBeSeenCollection;

    private MovieCollection likedCollection;

    private List<MovieCollection> myCollections=new ArrayList<>();


    private List<MovieCollection> followedCollections = new ArrayList<>();

    //this one has to be incremented in service, with a trigger there would be an if statement for every movie added in a collection
    private int seen;

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<Integer> getProviderIds() {
        return providerIds;
    }

    public void setProviderIds(List<Integer> providerIds) {
        this.providerIds = providerIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public MovieCollection getSeenCollection() {
        return seenCollection;
    }

    public void setSeenCollection(MovieCollection seenCollection) {
        this.seenCollection = seenCollection;
    }

    public MovieCollection getToBeSeenCollection() {
        return toBeSeenCollection;
    }

    public void setToBeSeenCollection(MovieCollection toBeSeenCollection) {
        this.toBeSeenCollection = toBeSeenCollection;
    }

    public MovieCollection getLikedCollection() {
        return likedCollection;
    }

    public void setLikedCollection(MovieCollection likedCollection) {
        this.likedCollection = likedCollection;
    }

    public List<MovieCollection> getMyCollections() {
        return myCollections;
    }

    public void setMyCollections(List<MovieCollection> myCollections) {
        this.myCollections = myCollections;
    }

    public List<MovieCollection> getFollowedCollections() {
        return followedCollections;
    }

    public void setFollowedCollections(List<MovieCollection> followedCollections) {
        this.followedCollections = followedCollections;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}
