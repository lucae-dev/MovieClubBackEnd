package com.movies.movie.app.MovieRating;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.MovieComment.MovieComment;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.util.List;

@Entity
public class MovieRating {

    @EmbeddedId
    MovieRatingKey id;

    @ManyToOne
    @MapsId("userId")
    @JsonIgnore
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("movieId")
    @JsonIgnore
    @JoinColumn(name = "movie_id")
    Movie movie;

    @OneToMany(mappedBy = "movieRating")
    List<MovieComment> comments;

    private boolean seen;

    @DecimalMax(value = "10", message = "Max rating for a movie is 10")
    @DecimalMin(value = "0", message = "min rating for a movie is 0")
    private float rating;


    public MovieRating() {
    }

    public MovieRating(MovieRatingKey id, User user, Movie movie, boolean seen, float rating, String comment) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.seen = seen;
        this.rating = rating;
    }



    public MovieRatingKey getId() {
        return id;
    }

    public MovieRating( User user, Movie movie) {
        this.id = new MovieRatingKey(user.getId(), movie.getId());
        this.user = user;
        this.movie = movie;
    }

    public void setId(MovieRatingKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<MovieComment> getComments() {
        return comments;
    }

    public void addComment(MovieComment comment) {
        this.comments.add(comment);
    }
}
