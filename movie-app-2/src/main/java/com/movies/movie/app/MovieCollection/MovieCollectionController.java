package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
public class MovieCollectionController {


    private final MovieCollectionService movieCollectionService;

    @Autowired
    public MovieCollectionController(MovieCollectionService movieCollectionService) {
        this.movieCollectionService = movieCollectionService;
    }

    @PostMapping("/create")
    public MovieCollection createMovieCollection(@AuthenticationPrincipal User user,  @RequestBody MovieCollection movieCollection) {
        return movieCollectionService.createMovieCollection(user, movieCollection);
    }

    @PostMapping("/{id}/addMovie")
    public MovieCollection addMovie(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Movie movie) {
        return movieCollectionService.addMovie(user, id, movie);
    }

    @PostMapping("/{id}/addMovies")
    public MovieCollection addMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.addMovies(user, id, movies);
    }

    @PostMapping("/{id}/removeMovie")
    public MovieCollection removeMovie(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Movie movie) {
        return movieCollectionService.removeMovie(user, id, movie);
    }

    @PostMapping("/{id}/removeMovies")
    public MovieCollection removeMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.removeMovies(user, id, movies);
    }


    @GetMapping("/getDefault")
    public List<MovieCollection> getDefaultCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getDefaultCollections(user);
    }

    @GetMapping("/myCollections")
    public List<MovieCollection> getMyCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getMyCollections(user);
    }

    @GetMapping("/savedCollections")
    public List<MovieCollection> getSavedCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getSavedCollections(user);
    }

    @GetMapping("/all")
    public List<MovieCollection> getAllCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getAllCollections(user);
    }
}
