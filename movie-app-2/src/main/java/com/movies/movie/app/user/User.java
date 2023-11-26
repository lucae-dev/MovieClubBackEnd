package com.movies.movie.app.user;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.movies.movie.app.FCMToken.DeviceToken;
import com.movies.movie.app.MovieCollection.MovieCollection;
import com.movies.movie.app.MovieRating.MovieRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_user")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean locked = false;
    private boolean verified =  false;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> providerIds;
    // birthday !! Important for recommendations
    @NotNull(message = "email is required to register")
    @Column(unique = true)
    private String email;
    private String propic;
    //Firebase token for notifications !!!!!!!
   /* @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<MovieRating> movieRatings;
*/
    //@OneToMany(mappedBy = )
    //movie comments

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> following;

    private String biography;

    //incremented by triggers in database whenever a user gets a new follower
    private int followers_count;
    //incremented by triggers in database whenever a user starts to follow another one
    private int following_count;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seen_collection")
    private MovieCollection seenCollection;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_see")
    private MovieCollection toBeSeenCollection;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "liked_collection")
    private MovieCollection likedCollection;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieCollection> myCollections=new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_followed_collections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id"))
    private List<MovieCollection> followedCollections = new ArrayList<>();

    //this one has to be incremented in service, with a trigger there would be an if statement for every movie added in a collection
    private int seen;

    @OneToMany(mappedBy = "user")
    private Set<DeviceToken> deviceTokens;



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        // Exclude followers and following from hashCode
        return result;
    }

    public User() {
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }



    public User(Long id, String username, String email, String propic) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.propic = propic;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Integer> getProviderIds() {
        return providerIds;
    }

    public void setProviderIds(List<Integer> providerIds) {
        this.providerIds = providerIds;
    }



    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public MovieCollection getSeenCollection() {
        return seenCollection;
    }

    public void setSeenCollection(MovieCollection seenCollection) {
        this.seenCollection = seenCollection;
    }

    public MovieCollection getToBeSeenCollection() {
        return toBeSeenCollection;
    }

    public void setToBeSeenCollection(MovieCollection toBeSeenCollection) {
        this.toBeSeenCollection = toBeSeenCollection;
    }

    public MovieCollection getLikedCollection() {
        return likedCollection;
    }

    public void setLikedCollection(MovieCollection likedCollection) {
        this.likedCollection = likedCollection;
    }

    public List<MovieCollection> getMyCollections() {
        return myCollections;
    }

    public void setMyCollections(List<MovieCollection> myCollections) {
        this.myCollections = myCollections;
    }

    public List<MovieCollection> getFollowedCollections() {
        return followedCollections;
    }

    public void setFollowedCollections(List<MovieCollection> followedCollections) {
        this.followedCollections = followedCollections;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Set<DeviceToken> getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(Set<DeviceToken> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }
}
