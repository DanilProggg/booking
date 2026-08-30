package com.kridan.booking.controller;

import com.kridan.booking.controller.dto.LoginRequest;
import com.kridan.booking.controller.dto.RegistrationRequest;
import com.kridan.booking.controller.dto.UserResponse;
import com.kridan.booking.entity.AppUserDetails;
import com.kridan.booking.entity.User;
import com.kridan.booking.service.AppUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AppUserDetailsService appUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();


    public AuthController(AppUserDetailsService appUserDetailsService, AuthenticationManager authenticationManager) {
        this.appUserDetailsService = appUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    private ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest){
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

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse httpResponse) {

        Authentication unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(request.email(), request.password());

        Authentication authenticated = authenticationManager.authenticate(unauthenticated);

        httpRequest.changeSessionId();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticated);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        AppUserDetails principal = (AppUserDetails) authenticated.getPrincipal();

        return ResponseEntity.ok(UserResponse.from(principal));
    }
}
