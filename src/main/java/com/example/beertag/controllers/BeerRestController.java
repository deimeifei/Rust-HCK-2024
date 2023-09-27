package com.example.beertag.controllers;

import com.example.beertag.models.Beer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private final List<Beer> beers;

    public BeerRestController() {
        beers = new ArrayList<>();

        beers.add(new Beer(1,"Glarus English Ale",4.6));
        beers.add(new Beer(2,"Rhombus Porter",5.0));
    }

    @GetMapping
    public List<Beer> getAll() {
        return beers;
    }

    @GetMapping("/{id}")
    public Beer getById(@PathVariable int id){
        return beers.stream()
                .filter(beer -> beer.getId()==id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Beer with id %d not found.",id)
                ));
    }

    @PostMapping
    public Beer create(@Valid @RequestBody Beer beer)
    {
        beers.add(beer);
        return beer;
    }
    @PutMapping("/{id}")
    public Beer update(@PathVariable int id, @Valid @RequestBody Beer beerBody)
    {
        Beer beer = getById(id);
        beer.setAbv(beerBody.getAbv());
        beer.setName(beerBody.getName());
        return beer;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id)
    {
        Beer beer = getById(id);
        beers.remove(beer);
    }
}
