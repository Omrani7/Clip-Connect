package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client extends User {
    private String phoneNumber;

    public Client() {
    }

    public Client(Integer id, String name, String email,String password, String phoneNumber) {
        super(id, name, email,password);
        this.phoneNumber = phoneNumber;
    }

    @OneToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
