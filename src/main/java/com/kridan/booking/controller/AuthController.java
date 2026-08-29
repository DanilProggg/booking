package com.kridan.booking.controller;

import com.kridan.booking.controller.dto.RegistrationRequest;
import com.kridan.booking.entity.User;
import com.kridan.booking.service.AppUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Slf4j
public class AuthController {
    private final AppUserDetailsService appUserDetailsService;

    public AuthController(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    private ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        try {
            User user = appUserDetailsService.createUser(registrationRequest.email(), registrationRequest.password());
            return ResponseEntity
                    .created(URI.create("/api/users/" + user.getId()))
                    .body(user);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }
    }
}
