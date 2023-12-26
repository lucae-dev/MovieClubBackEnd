package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.Notifications.NotificationService;
import com.movies.movie.app.TVSeries.TVSeries;
import com.movies.movie.app.TVSeries.TVSeriesRepository;
import com.movies.movie.app.WatchProvider.WatchProvider;
import com.movies.movie.app.WatchProvider.WatchProvidersContainer;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieDTO;
import com.movies.movie.app.movie.MovieRepository;
import com.movies.movie.app.movie.MovieService;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserDTO;
import com.movies.movie.app.user.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieCollectionService {
    @Autowired
    private MovieCollectionRepository movieCollectionRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TVSeriesRepository tvSeriesRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    public MovieCollectionDTO convertToDTO(MovieCollection movieCollection){
        UserDTO user = new UserDTO();
        user.setId(movieCollection.getOwner().getId());
        user.setUsername(movieCollection.getOwner().getUsername());
            MovieCollectionDTO movieCollectionDTO = new MovieCollectionDTO();
            movieCollectionDTO.setId(movieCollection.getId());
            movieCollectionDTO.setCreation_date(movieCollection.getCreation_date());
            movieCollectionDTO.setDescription(movieCollection.getDescription());
            movieCollectionDTO.setFollowCount(movieCollection.getFollowCount());
            movieCollectionDTO.setName(movieCollection.getName());
            movieCollectionDTO.setOwner(user);
            List<MovieDTO> movies = new ArrayList<>();
            for(int i=0;i<4 && i<movieCollection.getMovies().size();i++){
                MovieDTO movieT = new MovieDTO();
                movieT.setId(movieCollection.getMovies().get(i).getId());
                movieT.setPoster_path(movieCollection.getMovies().get(i).getPoster_path());
                movieT.setBackdrop_path(movieCollection.getMovies().get(i).getBackdrop_path());
                System.out.println(movieT.getId());
                movies.add(movieT);
                System.out.println( movies.get(i).getId());
            }
            for(MovieDTO movieDTO:movies){
                System.out.println(movieDTO.getId());
            }
            movieCollectionDTO.setMovies(movies);
        for(MovieDTO movieDTO:movieCollectionDTO.getMovies()){
            System.out.println(movieDTO.getId());
        }
            return movieCollectionDTO;
    }

    public List<MovieCollectionDTO> convertListToDTO(List<MovieCollection> movieCollections){
        List<MovieCollectionDTO> movieCollectionDTOS = new ArrayList<>();
        for(MovieCollection movieCollection:movieCollections){
            movieCollectionDTOS.add(convertToDTO(movieCollection));
        }
        return movieCollectionDTOS;
    }



    public List<MovieCollectionDTO> addFollowedCollection(Long userId, List<MovieCollectionDTO> movieCollectionDTOs){

      User user=  userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User not found"));


        List<MovieCollectionDTO> movieCollectionUserDTOs = convertListToDTO(user.getFollowedCollections());
        for(MovieCollectionDTO movieCollection:movieCollectionDTOs){
            if(movieCollectionUserDTOs.stream().anyMatch(movieCollectionDTO -> movieCollectionDTO.getId().equals(movieCollection.getId()))){
                movieCollection.setFollowed(Boolean.TRUE);
            }
        }
        return movieCollectionDTOs;
    }


    public List<MovieDTO> getCollectionMovies(User user2, Long collid, String language){
            User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        MovieCollection movieCollection = movieCollectionRepository.findById(collid).orElseThrow(()->new IllegalStateException("collection does not exist!"));

        List<MovieDTO> movies = new ArrayList<>();

        for(Movie m : movieCollection.getMovies()){
            MovieDTO moviet =new MovieDTO();
            moviet = movieService.convertToDTO(movieService.getMovieDetails(m.getId(), language));
            final Long idTemp = moviet.getId();
            if(user.getLikedCollection().getMovies().stream().anyMatch(movieLiked -> Objects.equals(movieLiked.getId(),idTemp))){
                moviet.setLiked(Boolean.TRUE);
            }
            else {
                moviet.setLiked(Boolean.FALSE);
            }
            movies.add(moviet);
        }
       return movies;
    }

    public List<MovieDTO> getSeenMovies(User user2, Long collid, String language){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));

        List<MovieDTO> movies = new ArrayList<>();

        for(Movie m : user.getSeenCollection().getMovies()){
            MovieDTO moviet =new MovieDTO();
            moviet = movieService.convertToDTO(movieService.addProviders(movieService.getMovieDetails(m.getId(), language)));
            final Long idTemp = moviet.getId();
            if(user.getLikedCollection().getMovies().stream().anyMatch(movieLiked -> Objects.equals(movieLiked.getId(),idTemp))){
                moviet.setLiked(Boolean.TRUE);
            }
            else {
                moviet.setLiked(Boolean.FALSE);
            }
            movies.add(moviet);
        }
        return movies;
    }

    public List<MovieCollectionDTO> getDefaultCollections(User user2) {
        List<MovieCollection> defaultCollections = new ArrayList<>();
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("user not found!"));
        defaultCollections.add(user.getSeenCollection());

        defaultCollections.add(user.getToBeSeenCollection());
        defaultCollections.add(user.getLikedCollection());
        for(MovieCollection coll : defaultCollections){
            System.out.println(coll.getName());
        }
        return  convertListToDTO(defaultCollections);
    }

    public List<MovieCollectionDTO> getMyCollections(User user2, Pageable pageable) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        Page<MovieCollection> movieCollectionsPage = movieCollectionRepository.findByOwner(user, pageable);

        return convertListToDTO(movieCollectionsPage.getContent());
    }


    public List<MovieCollectionDTO> getMyCollectionsIsMoviePresent(User user2, Long movieId, Pageable pageable ) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        List<MovieCollection> movieCollections = movieCollectionRepository.findByOwner(user, pageable).getContent();
        List<MovieCollectionDTO> movieCollectionDTOS = new ArrayList<>();
        for(MovieCollection movieCollection: movieCollections){
            MovieCollectionDTO movieCollectionDTO = convertToDTO(movieCollection);
            movieCollectionDTO.setMoviePresent(Boolean.FALSE);
            if(movieCollection.getMovies().stream().anyMatch(movie1 -> Objects.equals(movie1.getId(),movieId))){
                movieCollectionDTO.setMoviePresent(Boolean.TRUE);
            }
            movieCollectionDTOS.add(movieCollectionDTO);
        }
        return movieCollectionDTOS;
    }


    public List<MovieCollectionDTO> getSavedCollections(User user2, Pageable pageable) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        List<MovieCollection> followedCollections = movieCollectionRepository.findFollowedByUser(user.getId(), pageable).getContent();
        return addFollowedToCollections(user, convertListToDTO(followedCollections));
    }


/*
    public List<MovieCollectionDTO> addFollowingToDTOList(User user2, List<MovieCollectionDTO> movieCollectionDTOS){
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        for(MovieCollectionDTO movieCollectionDTO:movieCollectionDTOS){
            if(user.getFollowedCollections().stream().anyMatch(followedCollection -> Objects.equals(followedCollection.getId(),movieCollectionDTO.getId()))){
                movieCollectionDTO.setFollowed(Boolean.TRUE);
            }
            else {
                movieCollectionDTO.setFollowed(Boolean.FALSE);
            }
        }
        return movieCollectionDTOS;
    }
*/

    public List<MovieCollectionDTO> searchByKeyword(User user, String keyword, Pageable pageable){
        Page<MovieCollection> collections = movieCollectionRepository.searchByKeyword(keyword, pageable);
        List<MovieCollectionDTO> collectionsDTOs = collections.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return addFollowedToCollections(user, collectionsDTOs);

    }
    public MovieCollectionDTO createMovieCollection(User user2, MovieCollection movieCollection) {
        movieCollection.setCreation_date(LocalDateTime.now());
        movieCollection.setOwner(user2);
        movieCollection.setVisible(Boolean.TRUE);
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        user.getMyCollections().add(movieCollection);
        user = userRepository.saveAndFlush(user);
        return convertToDTO(user.getMyCollections().stream().filter(movieCollection1 -> movieCollection1.getCreation_date().equals(movieCollection.getCreation_date())).findFirst().orElseThrow());
    }

    public boolean addMovie(User user, Long id, Movie movie) {
        if(!movieRepository.existsById(movie.getId())){
            movieRepository.save(movie);
        }
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            if(movieCollection.getMovies().stream().anyMatch(movie1 -> Objects.equals(movie1.getId(),movie.getId()))){

            }
            else{
                movieCollection.getMovies().add(movie);
                movieCollectionRepository.save(movieCollection);
            }
            return movieCollection.getMovies().stream().anyMatch(movie1 -> Objects.equals(movie1.getId(),movie.getId()));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }


    public boolean addTVSeries(User user, Long id, TVSeries tvSeries) {
        if(!tvSeriesRepository.existsById(tvSeries.getId())){
            tvSeriesRepository.save(tvSeries);
        }
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            if(movieCollection.getTvSeries().stream().anyMatch(tvSeries1 -> Objects.equals(tvSeries1.getId(),tvSeries.getId()))){

            }
            else{
                movieCollection.getTvSeries().add(tvSeries);
                movieCollectionRepository.save(movieCollection);
            }
            return movieCollection.getTvSeries().stream().anyMatch(tvSeries1 -> Objects.equals(tvSeries1.getId(),tvSeries.getId()));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }

    public boolean addToSeen(User user2, Movie movie) {
        if(!movieRepository.existsById(movie.getId())){
            movieRepository.save(movie);
        }
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection seen = user.getSeenCollection();
        if (seen.getMovies().stream()
                .anyMatch(movie2 -> Objects.equals(movie2.getId(), movie.getId()))) {
            user.getToBeSeenCollection().getMovies().removeIf(m -> Objects.equals(m.getId(), movie.getId()));

            return Boolean.TRUE;
        } else {
            user.getToBeSeenCollection().getMovies().removeIf(m -> Objects.equals(m.getId(), movie.getId()));
            user.getSeenCollection().getMovies().add(movie);
            userRepository.save(user);
            return user.getSeenCollection().getMovies().stream().anyMatch(movie1 -> Objects.equals(movie1.getId(),movie.getId()));
        }

    }

    public boolean addToSeen(User user2, TVSeries tvSeries) {
        if(!tvSeriesRepository.existsById(tvSeries.getId())){
            tvSeriesRepository.save(tvSeries);
        }
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection seen = user.getSeenCollection();
        if (seen.getMovies().stream()
                .anyMatch(movie2 -> Objects.equals(movie2.getId(), tvSeries.getId()))) {
            user.getToBeSeenCollection().getMovies().removeIf(m -> Objects.equals(m.getId(), tvSeries.getId()));

            return Boolean.TRUE;
        } else {
            user.getToBeSeenCollection().getTvSeries().removeIf(t -> Objects.equals(t.getId(), tvSeries.getId()));
            user.getSeenCollection().getTvSeries().add(tvSeries);
            userRepository.save(user);
            return user.getSeenCollection().getTvSeries().stream().anyMatch(tvSeries1 -> Objects.equals(tvSeries1.getId(),tvSeries.getId()));
        }

    }


    public boolean addToBeSeen(User user2, Movie movie) {
        if(!movieRepository.existsById(movie.getId())){
            movieRepository.save(movie);
        }
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection toSee = user.getToBeSeenCollection();
        if (toSee.getMovies().stream()
                .anyMatch(movie2 -> Objects.equals(movie2.getId(), movie.getId()))) {
            user.getSeenCollection().getMovies().removeIf(m -> Objects.equals(m.getId(), movie.getId()));

            return Boolean.TRUE;
        } else {


            user.getSeenCollection().getMovies().removeIf(m -> Objects.equals(m.getId(), movie.getId()));

            user.getToBeSeenCollection().getMovies().add(movie);
            userRepository.save(user);
            return user.getToBeSeenCollection().getMovies().stream().anyMatch(movie1 -> Objects.equals(movie1.getId(),movie.getId()));
        }

    }


    public boolean addToBeSeen(User user2, TVSeries tvSeries) {
        if(!tvSeriesRepository.existsById(tvSeries.getId())){
            tvSeriesRepository.save(tvSeries);
        }
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection toSee = user.getToBeSeenCollection();
        if (toSee.getTvSeries().stream()
                .anyMatch(tvSeries1 -> Objects.equals(tvSeries1.getId(), tvSeries.getId()))) {
            user.getSeenCollection().getTvSeries().removeIf(t -> Objects.equals(t.getId(), tvSeries.getId()));

            return Boolean.TRUE;
        } else {


            user.getSeenCollection().getTvSeries().removeIf(t -> Objects.equals(t.getId(), tvSeries.getId()));

            user.getToBeSeenCollection().getTvSeries().add(tvSeries);
            userRepository.save(user);
            return user.getToBeSeenCollection().getTvSeries().stream().anyMatch(tvSeries1 -> Objects.equals(tvSeries1.getId(),tvSeries.getId()));
        }

    }

    public boolean like(User user2, Movie movie) {
        if(!movieRepository.existsById(movie.getId())){
            movieRepository.save(movie);
        }
        System.out.println("Ok BRAH 1");

        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection likedCollection = user.getLikedCollection();
        if (likedCollection.getMovies().stream()
                .anyMatch(movie2 -> movie2.getId() == movie.getId())) {


        } else {
            System.out.println("Ok BRAH else");

            user.getLikedCollection().getMovies().add(movie);
            userRepository.saveAndFlush(user);
        }

        if(user.getLikedCollection().getMovies().stream()
                .anyMatch(movie2 -> movie2.getId() == movie.getId())){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public boolean like(User user2, TVSeries tvSeries) {
        if(!tvSeriesRepository.existsById(tvSeries.getId())){
            tvSeriesRepository.save(tvSeries);
        }
        System.out.println("Ok BRAH 1");

        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection likedCollection = user.getLikedCollection();
        if (likedCollection.getTvSeries().stream()
                .anyMatch(tvSeries1 -> tvSeries1.getId() == tvSeries.getId())) {


        } else {
            System.out.println("Ok BRAH else");

            user.getLikedCollection().getTvSeries().add(tvSeries);
            userRepository.saveAndFlush(user);
        }

        if(user.getLikedCollection().getTvSeries().stream()
                .anyMatch(tvSeries1 -> tvSeries1.getId() == tvSeries.getId())){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public boolean dislike(User user2, Movie movie) {

        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection likedCollection = user.getLikedCollection();
        if (likedCollection.getMovies().stream()
                .anyMatch(movie2 ->Objects.equals( movie2.getId() , movie.getId()))) {

            user.getLikedCollection().getMovies().removeIf(m->movie.getId().equals(m.getId()));
            userRepository.save(user);
            System.out.println("remove from liked");

        }
        if(user.getLikedCollection().getMovies().stream()
                .anyMatch(movie2 -> movie2.getId() == movie.getId())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean dislike(User user2, TVSeries tvSeries) {

        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection likedCollection = user.getLikedCollection();
        if (likedCollection.getTvSeries().stream()
                .anyMatch(tvSeries1 ->Objects.equals( tvSeries1.getId() , tvSeries.getId()))) {

            user.getLikedCollection().getTvSeries().removeIf(t->tvSeries.getId().equals(t.getId()));
            userRepository.save(user);
            System.out.println("remove from liked");

        }
        if(user.getLikedCollection().getTvSeries().stream()
                .anyMatch(tvSeries1 -> tvSeries1.getId() == tvSeries.getId())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Long followCollection(User user2, Long id ) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        MovieCollection collection = movieCollectionRepository.findById(id).orElseThrow(()->new IllegalStateException(" collection not found!"));

        if (user.getFollowedCollections().stream()
                .anyMatch(collection1 -> Objects.equals(collection1.getId() , id))) {

            return id;
        } else {
            user.getFollowedCollections().add(collection);
            userRepository.save(user);
            return collection.getId();

        }

    }


    public MovieCollectionDTO addMovies(User user, Long id, List<Movie> movies) {
            movieRepository.saveAll(movies);

        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getMovies().addAll(movies);
            return convertToDTO(movieCollectionRepository.save(movieCollection));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }


    public MovieCollectionDTO addTVSeries(User user, Long id, List<TVSeries> tvSeries) {
        tvSeriesRepository.saveAll(tvSeries);

        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            movieCollection.getTvSeries().addAll(tvSeries);
            return convertToDTO(movieCollectionRepository.save(movieCollection));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }

    }


    public MovieCollectionDTO setDescription(User user2, Long id, String description) {
       User user=  userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("Collection not found"));
       MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(()->new IllegalStateException("Collection not found"));
       if(user.getId()==movieCollection.getOwner().getId()){
           movieCollection.setDescription(description);
       }
      return convertToDTO(movieCollectionRepository.save(movieCollection));


    }

    public Boolean removeMovie(Long userId, Long id, Movie movie) {

        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("user not found"));
MovieCollection movieCollection = user.getMyCollections().stream().filter(movieCollection1 -> movieCollection1.getId().equals(id)).findFirst().orElseThrow(()->new IllegalArgumentException("You cannot modify this collection since you are not the owner!"));
        if (movieCollection.getMovies().stream()
                .anyMatch(movie2 -> movie2.getId() == movie.getId())) {

            movieCollection.getMovies().removeIf(m->movie.getId().equals(m.getId()));
            userRepository.save(user);
        }

            return Boolean.FALSE;



    }


    public Boolean removeSeries(Long userId, Long id, TVSeries tvSeries) {

        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("user not found"));
        MovieCollection movieCollection = user.getMyCollections().stream().filter(movieCollection1 -> movieCollection1.getId().equals(id)).findFirst().orElseThrow(()->new IllegalArgumentException("You cannot modify this collection since you are not the owner!"));
        if (movieCollection.getTvSeries().stream()
                .anyMatch(tvSeries1 -> tvSeries1.getId() == tvSeries.getId())) {

            movieCollection.getTvSeries().removeIf(t->tvSeries.getId().equals(t.getId()));
            userRepository.save(user);
        }

        return Boolean.FALSE;



    }


    public MovieCollectionDTO removeMovies(User user, Long id, List<Movie> movies) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {


            Set<Long> idsInMovieCollection = movies.stream()
                    .map(Movie::getId)
                    .collect(Collectors.toSet());

            movieCollection.getMovies().removeIf(movie -> idsInMovieCollection.contains(movie.getId()));

            return convertToDTO(movieCollectionRepository.save(movieCollection));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }
        //search movie collection by name
    }

    public MovieCollectionDTO removeTVSerieses(User user, Long id, List<TVSeries> tvSeries) {
        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {


            Set<Long> idsInMovieCollection = tvSeries.stream()
                    .map(TVSeries::getId)
                    .collect(Collectors.toSet());

            movieCollection.getTvSeries().removeIf(tvSeries1 -> idsInMovieCollection.contains(tvSeries1.getId()));

            return convertToDTO(movieCollectionRepository.save(movieCollection));
        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }
        //search movie collection by name
    }

    public MovieCollectionDTO addFollowedToCollection(List<MovieCollectionDTO> followedCollectionDTOS, MovieCollectionDTO movieCollectionDTO){
        if(followedCollectionDTOS.stream().anyMatch(movieCollectionDTO1 -> Objects.equals(movieCollectionDTO1.getId(), movieCollectionDTO.getId()))){
            movieCollectionDTO.setFollowed(Boolean.TRUE);
            System.out.println("TRUE KTMMMM");
        }
        return movieCollectionDTO;
    }

    public List<MovieCollectionDTO> addFollowedToCollections(User userMain, List<MovieCollectionDTO> movieCollectionDTOs){
        User userMe = userRepository.findById(userMain.getId()).orElseThrow(()->new IllegalStateException("user not found"));
        List<MovieCollectionDTO> followedCollectionDTOS =convertListToDTO( userMe.getFollowedCollections());
        for(MovieCollectionDTO movieCollectionDTO1: movieCollectionDTOs){
            addFollowedToCollection(followedCollectionDTOS,movieCollectionDTO1);
        }
        return movieCollectionDTOs;
    }

    public List<MovieCollectionDTO> getUserCollections(User userMain, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("user not found"));


        return addFollowedToCollections(userMain, convertListToDTO(movieCollectionRepository.findByOwner(user, pageable).getContent()));

    }

    public MovieCollectionDTO getUserSeenCollection(User userMain, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("user not found"));
        User userMe = userRepository.findById(userMain.getId()).orElseThrow(()->new IllegalStateException("user not found"));


        return addFollowedToCollection(convertListToDTO( userMe.getFollowedCollections()), convertToDTO(user.getSeenCollection()));

    }


    public boolean addToFavourite(Long userId, Long collectionId) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User not found"));
        System.out.println(" addFavourite");


        MovieCollection movieCollection = movieCollectionRepository.findById(collectionId).orElseThrow(()->new IllegalStateException("Collection not found"));
        if(user.getFollowedCollections().contains(movieCollection)) {
            return Boolean.TRUE;
        }
        movieCollection.setFollowCount(movieCollection.getFollowCount()+1);
        user.getFollowedCollections().add(movieCollection);
         userRepository.save(user);
         movieCollectionRepository.save(movieCollection);
        if(user.getFollowedCollections().contains(movieCollection)) {
            System.out.println("tutto ok, addFavourite");

            notificationService.likedCollectionNotification(user, movieCollection.getOwner(), movieCollection.getName());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean removeFromFavourite(Long userId, Long collectionId) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User not found"));
        MovieCollection movieCollection = movieCollectionRepository.findById(collectionId).orElseThrow(()->new IllegalStateException("Collection not found"));
        if(!user.getFollowedCollections().contains(movieCollection)) {
            return Boolean.FALSE;
        }
        user.getFollowedCollections().remove(movieCollection);
        movieCollection.setFollowCount(movieCollection.getFollowCount()-1);

        userRepository.save(user);
        movieCollectionRepository.save(movieCollection);

        return Boolean.FALSE;

    }
}
