package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;

import java.util.List;

public interface BeerService {

    List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder);

    Beer get(int id);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);

}
