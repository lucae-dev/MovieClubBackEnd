package com.movies.movie.app.MovieCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieDTO;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovieCollectionDTO {


    private Long Id;

    private String name;


    private UserDTO owner;

    private boolean visible;

    private String description;
    private LocalDateTime creation_date;



    private List<MovieDTO> movies;

    private List<UserDTO> followers;

    private boolean moviePresent;

    private int followCount;

    private boolean isFollowed;

    public MovieCollectionDTO() {
    }

    public boolean isMoviePresent() {
        return moviePresent;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public void setMoviePresent(boolean moviePresent) {
        this.moviePresent = moviePresent;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }

    public List<UserDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserDTO> followers) {
        this.followers = followers;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }
}
