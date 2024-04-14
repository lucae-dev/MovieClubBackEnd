package com.movies.movie.app.user;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% ORDER BY u.followers_count DESC")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.myCollections mc WHERE u.id = :userId AND mc.visible = TRUE")
    Optional<User> findUserWithVisibleCollections(@Param("userId") Long userId);

    @Query("SELECT u from User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);
}
