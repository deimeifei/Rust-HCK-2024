package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BeerRepositoryImpl implements BeerRepository {

    private final SessionFactory sessionFactory;


    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Beer> getBeer(FilterOptions filterOptions) {
       try(Session session = sessionFactory.openSession()){
           StringBuilder queryString = new StringBuilder("From Beer ");
           List<String> filterAttributes = new ArrayList<>();
           Map<String, Object> params = new HashMap<>();

           filterOptions.getName().ifPresent(value-> {
               filterAttributes.add(" name like :name ");
               params.put("name",String.format("%%%s%%", value));
           });

           filterOptions.getMinAbv().ifPresent(value-> {
               filterAttributes.add(" abv >= :minAbv ");
               params.put("minAbv", value);
           });

           filterOptions.getMaxAbv().ifPresent(value-> {
               filterAttributes.add(" abv <= :maxAbv ");
               params.put("maxAbv", value);
           });

           filterOptions.getStyleId().ifPresent(value-> {
               filterAttributes.add(" style.id = :styleId ");
               params.put("styleId", value);
           });

           if(!filterAttributes.isEmpty()) {
               queryString.append(" where ").append(String.join(" and ",filterAttributes));
           }

           queryString.append(generateOrderBy(filterOptions));
           Query<Beer> query = session.createQuery(queryString.toString(), Beer.class);
           query.setProperties(params);
           return query.list();
       }
    }

    private String generateOrderBy(FilterOptions filterOptions){
        if (filterOptions.getSortBy().isEmpty()){
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()){
            case "name":
                orderBy = "name";
                break;
            case "abv":
                orderBy = "abv";
                break;
            case "style":
                orderBy = "style.name";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if(filterOptions.getSortOrder().isPresent()
                && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")){
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }

    @Override
    public Beer getBeer(int id) {
        try (
                Session session = sessionFactory.openSession()
        ) {
            Beer beer = session.get(Beer.class, id);
            if (beer == null) {
                throw new EntityNotFoundException("Beer", id);
            }
            return beer;
        }
    }

    @Override
    public Beer getBeer(String name) {
        try (
                Session session = sessionFactory.openSession()
        ) {
            Query<Beer> query = session.createQuery("From Beer where name = :name", Beer.class);
            query.setParameter("name", name);
            List<Beer> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Beer", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Beer beer = getBeer(id);
            session.remove(beer);
            session.getTransaction().commit();
        }
    }

//    private static List<Beer> filter(List<Beer> beers, String name, Double minAbv, Double maxAbv,
//                                     Integer styleId, String sortBy, String sortOrder) {
//        List<Beer> result = beers;
//        result = filterByName(result, name);
//        result = filterByAbv(result, minAbv, maxAbv);
//        result = filterByStyle(result, styleId);
//        result = sortBy(result, sortBy);
//        result = order(result, sortOrder);
//        return result;
//    }
//
//    private static List<Beer> filterByName(List<Beer> beers, String name) {
//        if (name != null && !name.isEmpty()) {
//            beers = beers.stream()
//                    .filter(beer -> containsIgnoreCase(beer.getName(), name))
//                    .collect(Collectors.toList());
//        }
//        return beers;
//    }
//
//    private static List<Beer> filterByAbv(List<Beer> beers, Double minAbv, Double maxAbv) {
//        if (minAbv != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getAbv() >= minAbv)
//                    .collect(Collectors.toList());
//        }
//
//        if (maxAbv != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getAbv() <= maxAbv)
//                    .collect(Collectors.toList());
//        }
//
//        return beers;
//    }
//
//    private static List<Beer> filterByStyle(List<Beer> beers, Integer styleId) {
//        if (styleId != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getStyle().getId() == styleId)
//                    .collect(Collectors.toList());
//        }
//        return beers;
//    }
//
//    private static List<Beer> sortBy(List<Beer> beers, String sortBy) {
//        if (sortBy != null && !sortBy.isEmpty()) {
//            switch (sortBy.toLowerCase()) {
//                case "name":
//                    beers.sort(Comparator.comparing(Beer::getName));
//                    break;
//                case "abv":
//                    beers.sort(Comparator.comparing(Beer::getAbv));
//                case "style":
//                    beers.sort(Comparator.comparing(beer -> beer.getStyle().getName()));
//                    break;
//            }
//        }
//        return beers;
//    }
//
//    private static List<Beer> order(List<Beer> beers, String order) {
//        if (order != null && !order.isEmpty()) {
//            if (order.equals("desc")) {
//                Collections.reverse(beers);
//            }
//        }
//        return beers;
//    }
//
//    private static boolean containsIgnoreCase(String value, String sequence) {
//        return value.toLowerCase().contains(sequence.toLowerCase());
//    }


}
