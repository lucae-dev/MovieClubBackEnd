package com.movies.movie.app.MovieRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, MovieRatingKey> {
}
