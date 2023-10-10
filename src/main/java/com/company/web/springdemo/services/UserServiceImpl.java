package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BeerRepository beerRepository;

    public UserServiceImpl(UserRepository userRepository, BeerRepository beerRepository) {
        this.userRepository = userRepository;
        this.beerRepository = beerRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id, User headersUser) {
        if (id == headersUser.getId() || headersUser.isAdmin()) {
            return userRepository.getById(id);
        } else {
            throw new UnauthorizedOperationException("You are not authorized to browse user information.");
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public List<Beer> getWishList(User user, User headersUser) {
        if (user.getId() == headersUser.getId() || headersUser.isAdmin()) {
            return new ArrayList<>(userRepository.getById(user.getId()).getWishList());
        } else {
            throw new UnauthorizedOperationException("You are not authorized to browse user information.");
        }
    }

    @Override
    public void updateWishList(Beer beer, User user, User headersUser) {
        if (user.getId() == headersUser.getId() || headersUser.isAdmin()) {
            user.getWishList().add(beer);
            userRepository.updateWishList(user);
        } else {
            throw new UnauthorizedOperationException("You are not authorized to browse user information.");
        }
    }

    @Override
    public void deleteFromWishList(Beer beer, User user, User authUser) {
        if (user.getId() == authUser.getId() || authUser.isAdmin()) {
            if (!user.getWishList().remove(beer)) {
                throw new EntityNotFoundException("Beer", beer.getId());
            }
            userRepository.updateWishList(user);
        } else {
            throw new UnauthorizedOperationException("You are not authorized to browse user information.");
        }
    }
}
