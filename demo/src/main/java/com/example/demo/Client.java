package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Client extends User {
    private String phoneNumber;
    private String jdfh;
    public Client() {
    }

    public Client(Integer id, String name, String email, String phoneNumber) {
        super(id, name, email);
        this.phoneNumber = phoneNumber;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
