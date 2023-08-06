package com.movies.movie.app.MovieRating;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovieRatingKey implements Serializable {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public MovieRatingKey() {
    }

    public MovieRatingKey(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
