package com.example.beertag.controllers;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.models.Beer;
import com.example.beertag.services.BeerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private BeerService service;

    @Autowired
    public BeerRestController(BeerService service) {
//        ApplicationContext context = new
//                AnnotationConfigApplicationContext(BeanConfiguration.class);
//        this.service = context.getBean(BeerService.class);
        this.service = service;
    }

    @GetMapping
    public List<Beer> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Beer getById(@PathVariable int id){
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @PostMapping
    public Beer create(@Valid @RequestBody Beer beer)
    {
        try {
            service.create(beer);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return beer;
    }
    @PutMapping("/{id}")
    public Beer update(@PathVariable int id, @Valid @RequestBody Beer beerBody)
    {
        try {
            service.update(beerBody);
        } catch (EntityNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
        return beerBody;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id)
    {
        try{
            service.delete(id);
        } catch (EntityNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
