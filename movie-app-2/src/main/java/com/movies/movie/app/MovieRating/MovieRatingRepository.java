package com.movies.movie.app.MovieRating;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRatingRepository extends JpaRepository<MovieRating, MovieRatingKey> {
}
