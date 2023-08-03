package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MovieCollectionService {
    @Autowired
    private MovieCollectionRepository movieCollectionRepository;

    public MovieCollection createMovieCollection(User user, String title) {
        MovieCollection movieCollection = new MovieCollection(title, user);
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
