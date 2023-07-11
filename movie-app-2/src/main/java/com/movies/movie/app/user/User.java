package com.movies.movie.app.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movies.movie.app.MovieCollection.MovieCollection;
import com.movies.movie.app.MovieRating.MovieRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean locked = false;
    private boolean verified =  false;
    // birthday !! Important for recommendations
    @NotNull(message = "email is required to register")
    private String email;
    private String propic;
    //Firebase token for notifications !!!!!!!
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<MovieRating> movieRatings;

    //@OneToMany(mappedBy = )
    //movie comments

    @ManyToMany
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<User> following;

    //incremented by triggers in database whenever a user gets a new follower
    private int followers_count;
    //incremented by triggers in database whenever a user starts to follow another one
    private int following_count;

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MovieCollection> myCollections;

    //"Followed collections"
    @ManyToMany
    @JoinTable(name = "user_saved_coll",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id"))
    private List<User> savCollections;


    //this one has to be incremented in service, with a trigger there would be an if statement for every movie added in a collection
    private int seen;


    public User() {
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(Long id, String username, String email, String propic, Set<MovieRating> movieratings) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.propic = propic;
        this.movieRatings = movieratings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public Set<MovieRating> getRatedMovies() {
        return movieRatings;
    }

    public void setMovies(Set<MovieRating> movieratings) {
        this.movieRatings = movieratings;
    }
}
