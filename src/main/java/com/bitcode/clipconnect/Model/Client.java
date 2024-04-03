package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends User {
    private String phoneNumber;
    public Client() {
    }

    public Client(User user) {
        super(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getVerificationCode());
    }
}