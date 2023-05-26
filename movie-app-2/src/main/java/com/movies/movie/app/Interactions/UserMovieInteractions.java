package com.movies.movie.app.Interactions;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

/*
* Save users interactions with movie:
* - if he has whatched it
* - if he has liked it
* - if he has rated it or 0
* - how many times he commented it
* - how many times he has shared it
* - how many times he has clicked on the movie details
*
* */

@Entity
public class UserMovieInteractions {
    @EmbeddedId
    private UserMovieInteractionsKey userMovieInteractions;

    private int click_count;

    private boolean seen;

    private boolean liked;

    private Float rating;

    private int comment_count;

    private int shared_count;

    public UserMovieInteractions() {
    }

    public UserMovieInteractions(UserMovieInteractionsKey userMovieInteractions, int click_count, boolean seen, boolean liked, Float rating, int comment_count, int shared_count) {
        this.userMovieInteractions = userMovieInteractions;
        this.click_count = click_count;
        this.seen = seen;
        this.liked = liked;
        this.rating = rating;
        this.comment_count = comment_count;
        this.shared_count = shared_count;
    }

    public UserMovieInteractionsKey getUserMovieInteractions() {
        return userMovieInteractions;
    }

    public void setUserMovieInteractions(UserMovieInteractionsKey userMovieInteractions) {
        this.userMovieInteractions = userMovieInteractions;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getShared_count() {
        return shared_count;
    }

    public void setShared_count(int shared_count) {
        this.shared_count = shared_count;
    }
}
