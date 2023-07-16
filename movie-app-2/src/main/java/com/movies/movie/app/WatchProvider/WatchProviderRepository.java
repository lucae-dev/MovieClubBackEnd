package com.movies.movie.app.WatchProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchProviderRepository extends JpaRepository<WatchProvider,Integer> {
}
