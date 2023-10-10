package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String name);

    void updateWishList(User user);

}
