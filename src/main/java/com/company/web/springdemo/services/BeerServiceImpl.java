package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository repository;

    @Autowired
    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        return repository.get(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
    }

    @Override
    public Beer get(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Beer beer) {
        boolean duplicateExists = true;
        try {
            repository.get(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        repository.create(beer);
    }

    @Override
    public void update(Beer beer) {
        boolean duplicateExists = true;
        try {
            Beer existingBeer = repository.get(beer.getName());
            if (existingBeer.getId() == beer.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        repository.update(beer);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }

}
