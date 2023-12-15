package com.vijay.databox.core.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserJwtDetails implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserJwtDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserJwtDetails [username=" + username + ", password=" + password + "]";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can customize this based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can customize this based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can customize this based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // You can customize this based on your requirements
    }
}
