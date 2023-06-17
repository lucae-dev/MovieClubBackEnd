package com.movies.movie.app.CountryProviders;

import com.movies.movie.app.WatchProvider.WatchProvider;
import jakarta.persistence.*;
import org.hibernate.boot.archive.spi.ArchiveEntry;

import java.util.Set;


@Entity
public class CountryProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String countrycode;
private String link;
@ManyToMany(cascade = CascadeType.PERSIST)
   private Set<WatchProvider> rent;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<WatchProvider> buy;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<WatchProvider> flatrate;


    public CountryProvider() {
    }


}
