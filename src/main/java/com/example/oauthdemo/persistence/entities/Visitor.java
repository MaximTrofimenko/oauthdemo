package com.example.oauthdemo.persistence.entities;

import com.example.oauthdemo.persistence.entities.enums.VisitorRole;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
public class Visitor extends PersistableEntity implements UserDetails {

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private VisitorRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null ? new ArrayList<>() {{ add((GrantedAuthority) () -> role.name()); }} : new ArrayList<>();
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

}