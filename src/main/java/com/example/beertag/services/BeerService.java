package com.example.beertag.services;

import com.example.beertag.models.Beer;

import java.util.List;

public interface BeerService {
    List<Beer> getAll();

    Beer getById(int id);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);
}
