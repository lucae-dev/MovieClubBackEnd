package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "collection")
public class MovieCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean visible;

    private String Description;

    @ManyToMany
    @JoinTable(name = "collection_movies",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;

    @ManyToMany(mappedBy = "savCollections")
    private List<User> followers;

    private int followCount;


}
