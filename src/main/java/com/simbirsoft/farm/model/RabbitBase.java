package com.simbirsoft.farm.model;

public class RabbitBase implements Entity {

    private Integer id;
    private String name;
    private String color;
    private int age;

    public RabbitBase() {
    }

    public RabbitBase(Integer id, String name, String color, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.color = color;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RabbitBase [id=" + id + ", name=" + name + ", color=" + color + ", age=" + age + "]";
    }

}