package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "barbers")
public class Barber extends User {
    private String shopName;

    public Barber() {
    }

    public Barber(User user) {
        super(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getVerificationCode());

    }

}
