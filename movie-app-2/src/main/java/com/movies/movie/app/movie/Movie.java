package com.movies.movie.app.movie;

import com.movies.movie.app.Genre.Genre;
import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.util.List;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "title is required")
    private String title;
    private int year;
    // modifica come relazione uno a molti generi e crea classe genere!
    @OneToMany
    @JoinTable(name = "movie_genres",
            joinColumns =@JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
    private String description;
    private double totpoints;
    private int votes;
    @OneToMany(mappedBy = "movie")
    Set<MovieRating> ratings;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "follows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    private Set<User> following;

    //Collections relationship


    public Movie() {
    }

    public Movie(String title, int year) {
        this.title = title;
        this.year = year;
        totpoints=0;
        votes=0;
    }

    public Movie(String title, int year, String description) {
        this.title = title;
        this.year = year;
        this.description = description;
        totpoints=0;
        votes=0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotpoints() {
        return totpoints;
    }

    public void setTotpoints(double totpoints) {
        this.totpoints = totpoints;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
