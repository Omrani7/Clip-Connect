package com.example.demo;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
   private String password;

    public User() {
    }

    public User(Integer id, String name, String email,String pass) {
        this.id = id;
        this.name = name;
        this.email = email;
        password = pass;
    }


    public Integer getUserId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CharSequence getPassword() {
    return password;}

    public void setPassword(String password) {
        this.password = password;
    }
}