package com.movies.movie.app.MovieComment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCommentRepository extends JpaRepository<MovieComment, Long> {
}
