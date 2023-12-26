package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.TVSeries.TVSeries;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieDTO;
import com.movies.movie.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

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

    @PostMapping("/{id}/addSeries")
    public boolean addSeries(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.addTVSeries(user, id, tvSeries);
    }


    //ASSOLUTAMENTE OTTIMIZZARE QUESTE TRE NON RITORNARE MOVIECOLLECTION E CHIEDERE SOLO ID MOVIE PER RIDURRE TRAFFICO!!!!!
    @PostMapping("/addToSeen")
    public boolean addMovieToSeen(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.addToSeen(user, movie);
    }

    @PostMapping("/addSeriesToSeen")
    public boolean addSeriesToSeen(@AuthenticationPrincipal User user, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.addToSeen(user, tvSeries);
    }

    @PostMapping("/addToBeSeen")
    public boolean addMovieToBeSeen(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.addToBeSeen(user, movie);
    }

    @PostMapping("/addSeriesToBeSeen")
    public boolean addMovieToBeSeen(@AuthenticationPrincipal User user, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.addToBeSeen(user, tvSeries);
    }

    @PostMapping("/addToLiked")
    public boolean addToLiked(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.like(user, movie);
    }

    @PostMapping("/addSeriesToLiked")
    public boolean addSeriesToLiked(@AuthenticationPrincipal User user, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.like(user, tvSeries);
    }

    //change names, for movies in movies collections here
    @PutMapping("/{collectionId}/addToFavourite")
    public boolean addToFavourite(@AuthenticationPrincipal User user, @PathVariable Long collectionId ) {
        return movieCollectionService.addToFavourite(user.getId(), collectionId);
    }

    @PutMapping("/{collectionId}/removeFromFavourite")
    public boolean removeFromFavourite(@AuthenticationPrincipal User user, @PathVariable Long collectionId ) {
        return movieCollectionService.removeFromFavourite(user.getId(), collectionId);
    }

    @PostMapping("/removeFromLiked")
    public boolean removeFromLiked(@AuthenticationPrincipal User user, @RequestBody Movie movie) {
        return movieCollectionService.dislike(user, movie);
    }

    @PostMapping("/removeSeriesFromLiked")
    public boolean removeSeriesFromLiked(@AuthenticationPrincipal User user, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.dislike(user, tvSeries);
    }



    @PostMapping("/{id}/addMovies")
    public MovieCollectionDTO addMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.addMovies(user, id, movies);
    }

    @PostMapping("/{id}/addSerieses")
    public MovieCollectionDTO addSerieses(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<TVSeries> tvSeries) {
        return movieCollectionService.addTVSeries(user, id, tvSeries);
    }

    @PostMapping("/{id}/setDescription")
    public MovieCollectionDTO setDescription(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody String description) {
        return movieCollectionService.setDescription(user, id, description);
    }

    @PostMapping("/{id}/removeMovie")
    public Boolean removeMovie(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Movie movie) {
        return movieCollectionService.removeMovie(user.getId(), id, movie);
    }

    @PostMapping("/{id}/removeSeries")
    public Boolean removeSeries(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody TVSeries tvSeries) {
        return movieCollectionService.removeSeries(user.getId(), id, tvSeries);
    }

    @PostMapping("/{id}/removeMovies")
    public MovieCollectionDTO removeMovies(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<Movie> movies) {
        return movieCollectionService.removeMovies(user, id, movies);
    }


    @PostMapping("/{id}/removeSerieses")
    public MovieCollectionDTO removeTVs(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody List<TVSeries> tvSeries) {
        return movieCollectionService.removeTVSerieses(user, id, tvSeries);
    }


    @GetMapping("/getDefault")
    public List<MovieCollectionDTO> getDefaultCollections(@AuthenticationPrincipal User user) {
        return movieCollectionService.getDefaultCollections(user);
    }

    @GetMapping("/myCollections")
    public List<MovieCollectionDTO> getMyCollections(@AuthenticationPrincipal User user,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable =  PageRequest.of(page, size);
        return movieCollectionService.getMyCollections(user, pageable);
    }




    @GetMapping("/myCollectionsIsMoviePresent")
    public List<MovieCollectionDTO> getMyCollectionsIsMoviePresent(@AuthenticationPrincipal User user,
                                                                   @RequestParam Long movieId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable =  PageRequest.of(page, size);

        return movieCollectionService.getMyCollectionsIsMoviePresent(user,movieId, pageable);
    }

    @GetMapping("/UserCollections")
    public List<MovieCollectionDTO> getUserCollections(@AuthenticationPrincipal User userMain,
                                                       @RequestParam Long userId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieCollectionService.getUserCollections(userMain, userId, pageable);
    }

    @GetMapping("/savedCollections")
    public List<MovieCollectionDTO> getSavedCollections(@AuthenticationPrincipal User user,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieCollectionService.getSavedCollections(user, pageable);
    }

    @GetMapping("/getMovies")
    public List<MovieDTO> getMoviesFromColl(@AuthenticationPrincipal User user, @RequestParam Long id, @RequestParam String language){
        return movieCollectionService.getCollectionMovies(user,id, UriUtils.decode(language, "UTF-8"));
    }

    @GetMapping("/getSeen")
    public List<MovieDTO> getMoviesFromSeen(@AuthenticationPrincipal User user, @RequestParam Long id, @RequestParam String language){
        return movieCollectionService.getSeenMovies(user,id, language);
    }
}
