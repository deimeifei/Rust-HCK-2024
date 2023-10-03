package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Style;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StyleRepositoryImpl implements StyleRepository {

    private final List<Style> styles;

    public StyleRepositoryImpl() {
        styles = new ArrayList<>();
        styles.add(new Style(1, "Special Ale"));
        styles.add(new Style(2, "English Porter"));
        styles.add(new Style(3, "Indian Pale Ale"));
    }

    @Override
    public List<Style> get() {
        return styles;
    }

    @Override
    public Style get(int id) {
        return styles.stream()
                .filter(style -> style.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Style", id));
    }

}
