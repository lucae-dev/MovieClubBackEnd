package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieDTO;
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
    public MovieCollectionDTO createMovieCollection(@AuthenticationPrincipal User user,  @RequestBody MovieCollection movieCollection) {
        return movieCollectionService.createMovieCollection(user, movieCollection);
    }

    @PostMapping("/{id}/addMovie")
    public boolean addMovie(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Movie movie) {
        return movieCollectionService.addMovie(user, id, movie);
    }


    //ASSOLUTAMENTE OTTIMIZZARE QUESTE TRE NON RITORNARE MOVIECOLLECTION E CHIEDERE SOLO ID MOVIE PER RIDURRE TRAFFICO!!!!!
    @PostMapping("/addToSeen")
    public boolean addMovieToSeen(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.addToSeen(user, movie);
    }

    @PostMapping("/addToBeSeen")
    public boolean addMovieToBeSeen(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.addToBeSeen(user, movie);
    }
    @PostMapping("/addToLiked")
    public boolean addToLiked(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.like(user, movie);
    }
    @PostMapping("/removeFromLiked")
    public boolean removeFromLiked(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.dislike(user, movie);
    }


    @PostMapping("/{id}/addMovies")
    public MovieCollectionDTO addMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.addMovies(user, id, movies);
    }

    @PostMapping("/{id}/removeMovie")
    public Boolean removeMovie(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Movie movie) {
        return movieCollectionService.removeMovie(user, id, movie);
    }

    @PostMapping("/{id}/removeMovies")
    public MovieCollectionDTO removeMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.removeMovies(user, id, movies);
    }


    @GetMapping("/getDefault")
    public List<MovieCollectionDTO> getDefaultCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getDefaultCollections(user);
    }

    @GetMapping("/myCollections")
    public List<MovieCollectionDTO> getMyCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getMyCollections(user);
    }

    @GetMapping("/myCollectionsIsMoviePresent")
    public List<MovieCollectionDTO> getMyCollectionsIsMoviePresent(@AuthenticationPrincipal User user, @RequestParam Long movieId) {
        return movieCollectionService.getMyCollectionsIsMoviePresent(user,movieId);
    }

    @GetMapping("/savedCollections")
    public List<MovieCollectionDTO> getSavedCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getSavedCollections(user);
    }

    @GetMapping("/all")
    public List<MovieCollectionDTO> getAllCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getAllCollections(user);
    }


    @GetMapping("/getMovies")
    public List<MovieDTO> getMoviesFromColl(@AuthenticationPrincipal User user, @RequestParam Long id, @RequestParam String language){
        return movieCollectionService.getCollectionMovies(user,id, language);
    }
}
