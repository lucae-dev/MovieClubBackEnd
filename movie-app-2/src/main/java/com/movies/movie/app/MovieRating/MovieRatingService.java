package com.movies.movie.app.MovieRating;


import com.movies.movie.app.MovieComment.MovieComment;
import com.movies.movie.app.movie.Movie;
import com.movies.movie.app.movie.MovieRepository;
import com.movies.movie.app.user.User;
import com.movies.movie.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;

@Service
public class MovieRatingService {

    @Autowired
    MovieRatingRepository movieRatingsRepository;

    @Autowired
    MovieRepository movieRepository;


/*
    public void likeMovie(User user, Long movie_id){

        if(!movieRepository.existsById(movie_id) || user.isCredentialsNonExpired())
        {
            throw new IllegalStateException("User or movie not found");
        }
        Movie movie = movieRepository.findById(movie_id).get();

        if(movieRatingsRepository.existsById(new MovieRatingKey(user, movie))){
            MovieRating movieRating = movieRatingsRepository.findById(new MovieRatingKey(user, movie)).get();
            movieRating.setLiked(Boolean.FALSE);
            movieRatingsRepository.save(movieRating);
        }
        MovieRating movieRating = new MovieRating(user, movie);
        movieRating.setLiked(Boolean.TRUE);
        movieRatingsRepository.save(movieRating);
    }

 */

    public void rate(User user, Long movie_id, float rate){
        if(!movieRepository.existsById(movie_id) || user.isCredentialsNonExpired())
        {
            throw new IllegalStateException("User or movie not found");
        }
        Movie movie = movieRepository.findById(movie_id).get();
        MovieRatingKey mvKey = new MovieRatingKey(user,movie);

        MovieRating movieRating = new MovieRating(user, movie);

        if(movieRatingsRepository.existsById(mvKey)) {
             movieRating = movieRatingsRepository.findById(mvKey).get();
        }

        movieRating.setRating(rate);

        movieRatingsRepository.save(movieRating);
        movie.setTotpoints(movie.getTotpoints() + rate);
        movie.setVotes(movie.getVotes()+1);
        movieRepository.save(movie);
    }

    public void comment(User user, Long movie_id, String comment){
        if(!movieRepository.existsById(movie_id) || user.isCredentialsNonExpired())
        {
            throw new IllegalStateException("User or movie not found");
        }
        Movie movie = movieRepository.findById(movie_id).get();
        MovieRatingKey mvKey = new MovieRatingKey(user,movie);

        MovieRating movieRating = new MovieRating(user, movie);

        if(movieRatingsRepository.existsById(mvKey)) {
            movieRating = movieRatingsRepository.findById(mvKey).get();
        }

        movieRating.setMainComment(comment);

        movieRatingsRepository.save(movieRating);
    }

    public void rate_and_comment(User user, Long movie_id, float rate, String comment){
        if(!movieRepository.existsById(movie_id) || user.isCredentialsNonExpired())
        {
            throw new IllegalStateException("User or movie not found");
        }
        Movie movie = movieRepository.findById(movie_id).get();
        MovieRatingKey mvKey = new MovieRatingKey(user,movie);

        MovieRating movieRating = new MovieRating(user, movie);

        if(movieRatingsRepository.existsById(mvKey)) {
            movieRating = movieRatingsRepository.findById(mvKey).get();
        }

        movieRating.setMainComment(comment);

        movieRating.setRating(rate);

        movieRatingsRepository.save(movieRating);
        movie.setTotpoints(movie.getTotpoints() + rate);
        movie.setVotes(movie.getVotes()+1);
        movieRepository.save(movie);
    }
/*
    public void markSeen(User user, Long movie_id){
        if(!movieRepository.existsById(movie_id) || user.isCredentialsNonExpired())
        {
            throw new IllegalStateException("User or movie not found");
        }
        Movie movie = movieRepository.findById(movie_id).get();
        MovieRatingKey mvKey = new MovieRatingKey(user,movie);

        MovieRating movieRating = new MovieRating(user, movie);

        if(movieRatingsRepository.existsById(mvKey)) {
            movieRating = movieRatingsRepository.findById(mvKey).get();
        }

        movieRating.setSeen(Boolean.TRUE);
        movieRatingsRepository.save(movieRating);
    }

*/
}
