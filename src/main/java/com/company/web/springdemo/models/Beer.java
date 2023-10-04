package com.company.web.springdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Beer {

    private int id;

    private String name;

    private double abv;

    private Style style;

    @JsonIgnore
    private User createdBy;

    public Beer() {
    }

    public Beer(int id, String name, double abv, User createdBy) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.createdBy = createdBy;
    }
    public Beer(int id, String name, double abv){
        this.id = id;
        this.name = name;
        this.abv = abv;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beer beer = (Beer) o;
        return id == beer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
