package com.movies.movie.app.MovieRating;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.MovieComment.MovieComment;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.util.List;

//qualsiasi interazione l'utente abbia avuto con un contenuto
@Entity
public class MovieRating {

    @EmbeddedId
    private MovieRatingKey id;

    private String mainComment;

    @OneToMany(mappedBy = "movieRating")
    private List<MovieComment> comments;

    private boolean seen;

    @DecimalMax(value = "10", message = "Max rating for a movie is 10")
    @DecimalMin(value = "0", message = "min rating for a movie is 0")
    private float rating;

    private Boolean liked;

    public MovieRating() {
    }

    public MovieRating(User user, Movie movie) {
        MovieRatingKey movieRatingKey = new MovieRatingKey(user,movie);
        this.id = id;
    }

    public MovieRatingKey getId() {
        return id;
    }

    public void setId(MovieRatingKey id) {
        this.id = id;
    }

    public void setComments(List<MovieComment> comments) {
        this.comments = comments;
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

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getMainComment() {
        return mainComment;
    }

    public void setMainComment(String mainComment) {
        this.mainComment = mainComment;
    }

    public List<MovieComment> getComments() {
        return comments;
    }

    public void addComment(MovieComment comment) {
        this.comments.add(comment);
    }
}
