package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {

}
