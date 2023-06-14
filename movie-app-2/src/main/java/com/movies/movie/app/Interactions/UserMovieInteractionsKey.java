package com.movies.movie.app.Interactions;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserMovieInteractionsKey implements Serializable {

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "content_id")
    private Long content_id;

    public UserMovieInteractionsKey() {
    }

    public UserMovieInteractionsKey(Long user_id, Long content_id) {
        this.user_id = user_id;
        this.content_id = content_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getMovie_id() {
        return content_id;
    }

    public void setMovie_id(Long content_id) {
        this.content_id = content_id;
    }
}
