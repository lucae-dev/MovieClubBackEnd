package com.movies.movie.app.MovieCollection;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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



    public List<MovieDTO> getCollectionMovies(User user2, Long collid, String language){
            User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("User not found"));
        MovieCollection movieCollection = movieCollectionRepository.findById(collid).orElseThrow(()->new IllegalStateException("collection does not exist!"));

        List<MovieDTO> movies = new ArrayList<>();

        for(Movie m : movieCollection.getMovies()){
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

    public List<MovieCollectionDTO> getMyCollections(User user2) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        return convertListToDTO(user.getMyCollections());
    }


    public List<MovieCollectionDTO> getMyCollectionsIsMoviePresent(User user2, Long movieId ) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        List<MovieCollection> movieCollections = user.getMyCollections();
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


    public List<MovieCollectionDTO> getSavedCollections(User user2) {
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        return convertListToDTO(user.getFollowedCollections());
    }

    public List<MovieCollectionDTO> getAllCollections(User user2) {
        List<MovieCollectionDTO> allCollections = new ArrayList<>();
        User user = userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException(" user not found!"));
        allCollections.addAll(getDefaultCollections(user));
        allCollections.addAll(getMyCollections(user));
        allCollections.addAll(getSavedCollections(user));
        return allCollections;
    }



    public MovieCollectionDTO createMovieCollection(User user2, MovieCollection movieCollection) {
        movieCollection.setCreation_date(LocalDateTime.now());
        movieCollection.setOwner(user2);
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



    public MovieCollectionDTO setDescription(User user2, Long id, String description) {

       User user=  userRepository.findById(user2.getId()).orElseThrow(()->new IllegalStateException("Collection not found"));
       MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(()->new IllegalStateException("Collection not found"));
       if(user.getId()==movieCollection.getOwner().getId()){
           movieCollection.setDescription(description);
       }
      return convertToDTO(movieCollectionRepository.save(movieCollection));


    }

    public Boolean removeMovie(User user, Long id, Movie movie) {

        MovieCollection movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));
        if (movieCollection.getOwner().getId() == user.getId()) {
            if (movieCollection.getMovies().stream()
                    .anyMatch(movie2 -> movie2.getId() == movie.getId())) {

                user.getLikedCollection().getMovies().removeIf(m->movie.getId().equals(m.getId()));
                userRepository.save(user);
            }
             movieCollection = movieCollectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie Collection not found"));

            if(movieCollection.getMovies().stream()
                    .anyMatch(movie2 -> movie2.getId() == movie.getId())){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;

        } else {
            throw new IllegalArgumentException("You cannot modify this collection since you are not the owner!");
        }
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
}
