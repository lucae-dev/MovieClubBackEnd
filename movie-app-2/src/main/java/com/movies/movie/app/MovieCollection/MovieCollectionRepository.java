package com.movies.movie.app.MovieCollection;

import com.movies.movie.app.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
@Repository
public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {
    @Query("SELECT mc FROM MovieCollection mc WHERE mc.name LIKE %:keyword% OR mc.description LIKE %:keyword% AND mc.visible=TRUE ORDER BY CASE WHEN mc.name LIKE %:keyword% THEN 1 ELSE 2 END, mc.followCount DESC")
    Page<MovieCollection> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
//select * from collection mc join user_followed_collections ufc on mc.id = ufc.collection_id where ufc.user_id = 1


  /*  @Query(value = "SELECT mc FROM collection mc JOIN user_followed_collections ufc ON mc.id = ufc.collection_id WHERE ufc.user_id = :userId",
            countQuery = "SELECT count(*) FROM collection mc JOIN user_followed_collections ufc ON mc.id = ufc.collection_id WHERE ufc.user_id = :userId",
            nativeQuery = true
    )*/
  @Query("SELECT mc FROM MovieCollection mc JOIN mc.followers u WHERE u.id = :userId")
  Page<MovieCollection> findFollowedByUser(@Param("userId") Long userId, Pageable pageable);

    Page<MovieCollection> findByOwner(User user, Pageable pageable);

    @Query("SELECT mc FROM MovieCollection mc WHERE mc.owner.id = :userId AND mc.type = :type")
    MovieCollection findCollectionByOwnerAndType(Long userId, MovieCollectionType type);


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO collection_movies (collection_id, movie_id) VALUES (?1, ?2)", nativeQuery = true)
    int addMovieToCollection(Long collectionId, Long movie_id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM collection_movies WHERE collection_id = ?1 AND movie_id = ?2", nativeQuery = true)
  int removeMovieFromCollection(Long collectionId, Long movie_id);
}
