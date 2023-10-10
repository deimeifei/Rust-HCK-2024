package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(int id, User headersUser);

    User getByUsername(String username);

    List<Beer> getWishList(User user, User headersUser);

    void updateWishList(Beer beer, User user, User authUser);

    void deleteFromWishList(Beer beer, User user, User authUser);

}
