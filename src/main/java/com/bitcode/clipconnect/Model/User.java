package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.security.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "verification_code", length = 10)
    private String verificationCode;

    @Column(name = "verified", columnDefinition = "BIT DEFAULT 0")
    private Boolean verified;

    //@Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //private Timestamp createdAt;
    //@Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    //private Timestamp updatedAt;

    public User(String name, String email, String password, String verificationCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        verified=false;
    }
    public User(){

    }
}