package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    public static final String DELETE_AUTHENTICATION_ERROR = "Only admins or the creator of the beer can delete it.";
    public static final String UPDATE_AUTHENTICATION_ERROR = "Only admins or the creator of the beer can modify it.";
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
    public Beer getByName(String name) {
        return repository.get(name);
    }

    @Override
    public void create(Beer beer,User user) {
        boolean duplicateExists = true;
        try {
            repository.get(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        repository.create(beer,user);
    }

    @Override
    public void update(Beer beer, User user) {
        if (user.equals(repository.get(beer.getId()).getCreatedBy()) || user.isAdmin()) {
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
        } else {
            throw new UnauthorizedOperationException(UPDATE_AUTHENTICATION_ERROR);
        }

    }

    @Override
    public void delete(int id, User user) {
        if (user.equals(get(id).getCreatedBy()) || user.isAdmin()) {
            repository.delete(id);
        } else {
            throw new UnauthorizedOperationException(DELETE_AUTHENTICATION_ERROR);
        }
    }

}
