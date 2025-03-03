package com.water.learnchild.model;

public class Child {
    private String key;
    private String name;
    private String password;
    private String gender;
    private Parent parent;
    private String age;
    private String image;

    public Child() {
    }

    public Child(String key, String name, String password, String gender, Parent parent, String age, String image) {
        this.key = key;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.parent = parent;
        this.age = age;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
