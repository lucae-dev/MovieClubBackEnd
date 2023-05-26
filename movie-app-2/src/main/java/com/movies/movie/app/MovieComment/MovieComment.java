package com.movies.movie.app.MovieComment;


import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class MovieComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Movie movie;

    private String comment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "comment_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Set<User> liked;

    @ManyToOne
    private MovieRating movieRating;

    //incremented by triggers in database
    private Integer likes;

    public MovieComment() {
    }

    public MovieComment(Long id, User user, Movie movie, String comment, Integer likes) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.comment = comment;
        this.likes = likes;
    }

    public MovieComment(User user, Movie movie, String comment, Integer likes) {

        this.user = user;
        this.movie = movie;
        this.comment = comment;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
