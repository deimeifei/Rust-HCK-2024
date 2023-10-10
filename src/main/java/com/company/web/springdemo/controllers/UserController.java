package com.company.web.springdemo.controllers;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.helpers.BeerMapper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.BeerService;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private final AuthenticationHelper authenticationHelper;

    private final BeerService beerService;

    private final BeerMapper beerMapper;

    @Autowired
    public UserController(UserService userService, AuthenticationHelper authenticationHelper, BeerService beerService, BeerMapper beerMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.beerService = beerService;
        this.beerMapper = beerMapper;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User headersUser = authenticationHelper.tryGetUser(headers);
            return userService.getById(id, headersUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/wish-list")
    public List<Beer> getWishList(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        User headersUser = authenticationHelper.tryGetUser(headers);
        try {
            User user = userService.getById(id, headersUser);
            return userService.getWishList(user, headersUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{id}/wish-list/{beerId}")
    public void updateWishList(@PathVariable int id, @PathVariable int beerId, @RequestHeader HttpHeaders headers) {
        try {
            User headersUser = authenticationHelper.tryGetUser(headers);
            User user = userService.getById(id, headersUser);
            Beer beer = beerService.get(beerId);
            userService.updateWishList(beer, user, headersUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/wish-list/{beerId}")
    public void deleteFromWishList(@PathVariable int id, @PathVariable int beerId, @RequestHeader HttpHeaders headers) {
        try {
            User headersUser = authenticationHelper.tryGetUser(headers);
            User user = userService.getById(id, headersUser);
            Beer beer = beerService.get(beerId);
            userService.deleteFromWishList(beer, user, headersUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
