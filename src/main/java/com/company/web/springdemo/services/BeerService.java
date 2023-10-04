package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface BeerService {

    List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder);

    Beer get(int id);
    Beer getByName(String name);

    void create(Beer beer, User user);

    void update(Beer beer, User user);

    void delete(int id, User user);

}
