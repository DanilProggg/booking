package com.kridan.booking.service;

import com.kridan.booking.entity.AppUserDetails;
import com.kridan.booking.entity.Role;
import com.kridan.booking.entity.User;
import com.kridan.booking.exceptions.UserAlreadyExistException;
import com.kridan.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new AppUserDetails(user);
    }

    public User createUser(String email, String rawPassword){
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistException(String.format("User with email {%s} already exist", email));
        }
        User user = new User(
            email,
            passwordEncoder.encode(rawPassword),
            Set.of(Role.USER)
        );

        userRepository.save(user);
        return user;

    }
}