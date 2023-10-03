package com.company.web.springdemo.controllers;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHENTICATION_ERROR = "The requested resource requires authentication.";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers){
        if(!headers.containsKey(AUTHORIZATION)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    AUTHENTICATION_ERROR);
        }

        try {
           String username = headers.getFirst(AUTHORIZATION);
           return userService.getByUsername(username);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid username.");
        }
    }
}
