package com.movies.movie.app.MovieCollection;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
@Repository
public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {
    @Query("SELECT mc FROM MovieCollection mc WHERE mc.name LIKE %:keyword% OR mc.description LIKE %:keyword% AND mc.visible=TRUE ORDER BY CASE WHEN mc.name LIKE %:keyword% THEN 1 ELSE 2 END, mc.followCount DESC")
    Page<MovieCollection> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
