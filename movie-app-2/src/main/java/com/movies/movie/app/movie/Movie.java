package com.movies.movie.app.movie;

import com.movies.movie.app.Genre.Genre;
import com.movies.movie.app.MovieRating.MovieRating;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Movie {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long movie_id;
    @Id
    private Long id;
    //langauge of the movie caracteristics ()
    private String original_language;

    @NotNull(message = "title is required")
    private String title;
    private LocalDate release_date;
    @Transient
    private List<Genre> genres;
    @ElementCollection
    private List<Integer> genre_ids;
    @ElementCollection
    private List<Integer> streaming_ids;
    @ElementCollection
    private List<Integer> rent_ids;
    @ElementCollection
    private List<Integer> buy_ids;
    //@Column(length = 1000)
    @Transient
    private String overview;
    private String description;
    private double totpoints;
    private float popularity;
    //vote_avarage and vote_count are from TMDB votes
    private float vote_average;
    private int vote_count;
    private String poster_path;
    private String backdrop_path;
    private int votes;




    private int runtime;
    private int budget;
    private Long revenue;
    private String tagline;

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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
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

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
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


    public List<Integer> getStreaming_ids() {
        return streaming_ids;
    }

    public void setStreaming_ids(List<Integer> streaming_ids) {
        this.streaming_ids = streaming_ids;
    }

    public List<Integer> getRent_ids() {
        return rent_ids;
    }

    public void setRent_ids(List<Integer> rent_ids) {
        this.rent_ids = rent_ids;
    }

    public List<Integer> getBuy_ids() {
        return buy_ids;
    }

    public void setBuy_ids(List<Integer> buy_ids) {
        this.buy_ids = buy_ids;
    }
}
