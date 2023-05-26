package com.movies.movie.app.MovieRating;


import com.movies.movie.app.MovieComment.MovieComment;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieRepository;
import com.movies.movie.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRatingService {

    @Autowired
    MovieRatingRepository movieRatingsRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    public void addMovie(Long user_id, Long movie_id){

        if(!movieRepository.existsById(movie_id) || !userRepository.existsById(user_id))
        {
            throw new IllegalStateException("User or movie not found");
        }

        MovieRating movieRating = new MovieRating(userRepository.findById(user_id).get(), movieRepository.findById(movie_id).get());
        movieRatingsRepository.save(movieRating);
    }

    public void rate(Long user_id, Long movie_id, float rate){
        MovieRatingKey mvKey = new MovieRatingKey(user_id,movie_id);
        if(!movieRatingsRepository.existsById(mvKey)){
            addMovie(user_id,movie_id);
        }

        Movie movie = movieRepository.findById(movie_id).get();
        movie.setTotpoints(movie.getTotpoints() + rate);
        movie.setVotes(movie.getVotes()+1);
        movieRepository.save(movie);
        MovieRating movieRating = movieRatingsRepository.findById(mvKey).get();
        movieRating.setRating(rate);

        movieRatingsRepository.save(movieRating);
    }

    public void comment(Long user_id, Long movie_id, String comment){
        MovieRatingKey mvKey = new MovieRatingKey(user_id,movie_id);
        if(!movieRatingsRepository.existsById(mvKey)){
            addMovie(user_id,movie_id);
        }

        MovieRating movieRating = movieRatingsRepository.findById(mvKey).get();

        //da sistemare, pensa come fare!!!!!
        movieRating.addComment(new MovieComment());

        movieRatingsRepository.save(movieRating);
    }

    public void rate_and_comment(Long user_id, Long movie_id, float rate, String comment){
        MovieRatingKey mvKey = new MovieRatingKey(user_id,movie_id);
        if(!movieRatingsRepository.existsById(mvKey)){
            addMovie(user_id,movie_id);
        }

        MovieRating movieRating = movieRatingsRepository.findById(mvKey).get();
        movieRating.setRating(rate);

        //da sistemare !!!
        movieRating.addComment(new MovieComment());

        movieRatingsRepository.save(movieRating);

    }

    public void markSeen(Long user_id, Long movie_id){
        MovieRatingKey mvKey = new MovieRatingKey(user_id,movie_id);
        if(!movieRatingsRepository.existsById(mvKey)){
            addMovie(user_id,movie_id);
        }

        MovieRating movieRating = movieRatingsRepository.findById(mvKey).get();
        movieRating.setSeen(!movieRating.isSeen());
        movieRatingsRepository.save(movieRating);
    }


}
