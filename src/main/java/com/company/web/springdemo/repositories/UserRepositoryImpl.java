package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository{
    private final List<User> users;

    public UserRepositoryImpl() {
        users = new ArrayList<>();
        users.add(new User(1,"pesho",true));
        users.add(new User(2,"tanya",false));
        users.add(new User(3,"chavo",true));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getById(int id) {
        return users.stream()
                .filter(user -> user.getId()==id)
                .findFirst().orElseThrow(
                        ()-> new EntityNotFoundException("User",id));
    }

    @Override
    public User getByName(String name) {
        return users.stream()
                .filter(user -> user.getUsername().equals(name))
                .findFirst().orElseThrow(
                        ()-> new EntityNotFoundException("User","name",name));

    }
}
