package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MovieCollectionService {
    @Autowired
    private MovieCollectionRepository movieCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<MovieCollection> getDefaultCollections(User user2) {
        List<MovieCollection> defaultCollections = new ArrayList<>();
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("user not found!"));
        defaultCollections.add(user.getSeenCollection());

        defaultCollections.add(user.getToBeSeenCollection());
        defaultCollections.add(user.getLikedCollection());
        for(MovieCollection coll : defaultCollections){
            System.out.println(coll.getName());
        }
        return defaultCollections;
    }

    public List<MovieCollection> getMyCollections(User user2) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        return user.getMyCollections();
    }

    public List<MovieCollection> getSavedCollections(User user2) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        return user.getFollowedCollections();
    }

    public List<MovieCollection> getAllCollections(User user2) {
        List<MovieCollection> allCollections = new ArrayList<>();
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        allCollections.addAll(getDefaultCollections(user));
        allCollections.addAll(getMyCollections(user));
        allCollections.addAll(getSavedCollections(user));
        return allCollections;
    }



    public MovieCollection createMovieCollection(User user2, MovieCollection movieCollection) {
        movieCollection.setCreation_date(LocalDate.now());
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        user.getMyCollections().add(movieCollection);
        return movieCollectionRepository.save(movieCollection);
    }

    public MovieCollection addMovie(User user, Long id, Movie movie) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getMovies().add(movie);
            return movieCollectionRepository.save(movieCollection);
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }

    public MovieCollection addMovies(User user, Long id, List<Movie> movies) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getMovies().addAll(movies);
            return movieCollectionRepository.save(movieCollection);
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }

    public MovieCollection removeMovie(User user, Long id, Movie movie) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getMovies().remove(movie);
            return movieCollectionRepository.save(movieCollection);
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }
    }

    public MovieCollection removeMovies(User user, Long id, List<Movie> movies) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getMovies().removeAll(movies);
            return movieCollectionRepository.save(movieCollection);
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }
        //search movie collection by name
    }
}
