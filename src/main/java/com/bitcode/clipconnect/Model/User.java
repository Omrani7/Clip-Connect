package com.bitcode.clipconnect.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String verificationCode;
    private Boolean verified;

    public User() {
    }

    public User(Integer id, String username, String email, String password,String verificationCode) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        verified=false;
    }
}
