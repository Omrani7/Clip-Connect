package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Barber extends User {
    private String shopName;
    public Barber() {
    }

    public Barber(Integer id, String name, String email,String password, String shopName) {
        super(id, name, email,password);
        this.shopName = shopName;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
