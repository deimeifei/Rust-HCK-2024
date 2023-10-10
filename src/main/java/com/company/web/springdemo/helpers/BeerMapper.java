package com.company.web.springdemo.helpers;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.BeerDto;
import com.company.web.springdemo.services.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    private final StyleService styleService;

    @Autowired
    public BeerMapper(StyleService styleService) {
        this.styleService = styleService;
    }

    public Beer fromDto(int id, BeerDto dto) {
        Beer beer = fromDto(dto);
        beer.setId(id);
        return beer;
    }

    public Beer fromDto(BeerDto dto) {
        Beer beer = new Beer();
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.get(dto.getStyleId()));
        return beer;
    }

}
