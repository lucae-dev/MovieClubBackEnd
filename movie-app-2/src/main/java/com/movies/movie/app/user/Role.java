package com.movies.movie.app.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

public enum Role {

    USER,
    ADMIN;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Assuming the role is stored as a field in the User class
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        // Add additional authorities if needed

        return authorities;
    }

}
