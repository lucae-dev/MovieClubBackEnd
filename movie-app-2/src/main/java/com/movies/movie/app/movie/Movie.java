package com.movies.movie.app.movie;

import com.movies.movie.app.Genre.Genre;
import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long movie_id;
    private Long id;
    //langauge of the movie caracteristics ()
    private String language;

    @NotNull(message = "title is required")
    private String title;
    private LocalDate release_date;
    // modifica come relazione uno a molti generi e crea classe genere!
    private String genres_ids;
    private String overview;
    private String description;
    private double totpoints;
    private float popularity;
    private float vote_avarage;
    private String poster_path;
    private String backdrop_path;
    private int votes;
    @OneToMany(mappedBy = "movie")
    Set<MovieRating> ratings;


    private int runtime;
    private int budget;
    private int revenue;
    private String tagline;
    List

/*
    @OneToMany
    @JoinTable(name = "movie_genres",
            joinColumns =@JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
    */

    //Collections relationship


    public Movie() {
    }

    public Movie(String title, LocalDate release_date) {
        this.title = title;
        this.release_date = release_date;
        totpoints=0;
        votes=0;
    }

    public Movie(String title, LocalDate release_date, String description) {
        this.title = title;
        this.release_date = release_date;
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

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public String getGenres_ids() {
        return genres_ids;
    }

    public void setGenres_ids(String genres_ids) {
        this.genres_ids = genres_ids;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVote_avarage() {
        return vote_avarage;
    }

    public void setVote_avarage(float vote_avarage) {
        this.vote_avarage = vote_avarage;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Set<MovieRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<MovieRating> ratings) {
        this.ratings = ratings;
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
