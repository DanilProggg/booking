package com.kridan.booking.entity;

import com.kridan.booking.entity.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AppUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public AppUserDetails(com.kridan.booking.entity.User user) {
        super(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                        .toList()
        );
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}