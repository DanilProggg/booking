package com.kridan.booking.controller.dto;


import com.kridan.booking.entity.AppUserDetails;
import com.kridan.booking.entity.Role;
import com.kridan.booking.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserResponse(Long id, String email, Set<String> roles) {

    public static UserResponse from(AppUserDetails principal) {
        return new UserResponse(
                principal.getId(),
                principal.getUsername(),
                principal.getAuthorities().stream()
                        .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                        .collect(Collectors.toSet())
        );
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet())
        );
    }
}
