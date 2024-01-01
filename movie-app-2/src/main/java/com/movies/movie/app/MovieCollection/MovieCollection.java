package com.movies.movie.app.MovieCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.TVSeries.TVSeries;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "collection")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MovieCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User owner;

    private MovieCollectionType type;

    private boolean visible;

    private String description;
    private LocalDateTime creation_date;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "collection_movies",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "collection_series",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "series_id"))
    private List<TVSeries> tvSeries;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followedCollections")
    private List<User> followers;


    private int followCount;


    public MovieCollection() {
    }

    public MovieCollection(String name,String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }
    public MovieCollection(String name,String description, User owner, MovieCollectionType type) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.type = type;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public MovieCollectionType getType() {
        return type;
    }

    public void setType(MovieCollectionType type) {
        this.type = type;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public List<TVSeries> getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(List<TVSeries> tvSeries) {
        this.tvSeries = tvSeries;
    }
}
