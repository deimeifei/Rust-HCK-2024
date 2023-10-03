package com.company.web.springdemo.controllers;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.helpers.BeerMapper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.BeerDto;
import com.company.web.springdemo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private final BeerService service;
    private final BeerMapper beerMapper;

    @Autowired
    public BeerRestController(BeerService service, BeerMapper beerMapper) {
        this.service = service;
        this.beerMapper = beerMapper;
    }

    @GetMapping
    public List<Beer> get(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minAbv,
            @RequestParam(required = false) Double maxAbv,
            @RequestParam(required = false) Integer styleId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        return service.get(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
    }

    @GetMapping("/{id}")
    public Beer get(@PathVariable int id) {
        try {
            return service.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Beer create(@Valid @RequestBody BeerDto beerDto) {
        try {
            Beer beer = beerMapper.fromDto(beerDto);
            service.create(beer);
            return beer;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Beer update(@PathVariable int id, @Valid @RequestBody BeerDto beerDto) {
        try {
            Beer beer = beerMapper.fromDto(id, beerDto);
            service.update(beer);
            return beer;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
