package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class BeerRepositorySqlImpl implements BeerRepository{

    private final String dbUrl, dbUsername, dbPassword;

    @Autowired
    public BeerRepositorySqlImpl(Environment env) {
        dbUrl = env.getProperty("database.url");
        dbUsername = env.getProperty("database.username");
        dbPassword = env.getProperty("database.password");
    }

    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        try {
             Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select beer_id, name, abv from beers");
            return getBeers(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Beer get(int id) {
        String query = "select beer_id, name, abv from beers where beer_id = ?";
        try(
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
                )
        {
            statement.setInt(1,id);
            try(
                    ResultSet resultSet = statement.executeQuery();
                    )
            {
                List<Beer> result = getBeers(resultSet);
                if(result.size()==0){
                    throw new EntityNotFoundException("Beer",id);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Beer get(String name) {
        String query = "select beer_id, name, abv from beers where name = ?";
        try(
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        )
        {
            statement.setString(1,name);
            try(
                    ResultSet resultSet = statement.executeQuery();
            )
            {
                List<Beer> result = getBeers(resultSet);
                if(result.size()==0){
                    throw new EntityNotFoundException("Beer","name", name);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void create(Beer beer, User user) {

    }

    @Override
    public void update(Beer beer) {

    }

    @Override
    public void delete(int id) {

    }
    private List<Beer> getBeers(ResultSet beersData) throws SQLException{
        List<Beer> beers  = new ArrayList<>();
        while(beersData.next()){
            Beer beer = new Beer(
                    beersData.getInt("beer_id"),
                    beersData.getString("name"),
                    beersData.getDouble("abv")
            );
            beers.add(beer);
        }
        return beers;
    }
}
