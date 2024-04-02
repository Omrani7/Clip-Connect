package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "barbers")
public class Barber extends User {
    private String shopName;
    public Barber() {
    }

    public Barber(Integer id, String name, String email,String password, String shopName) {
        super(id, name, email,password);
        this.shopName = shopName;
    }
    @OneToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
