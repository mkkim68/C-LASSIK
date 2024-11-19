package com.ssafy.a604.entity;

public class Composer {

    private final String name;
    private final String description;

    public Composer(String composer, String description) {
        this.name = composer;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Composer [name=" + name + ", description=" + description + "]";
    }

}