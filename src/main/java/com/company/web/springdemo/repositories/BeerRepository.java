package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;

import java.util.List;

public interface BeerRepository {

    List<Beer> getBeer(FilterOptions filterOptions);

    Beer getBeer(int id);

    Beer getBeer(String name);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);

}
