package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "verification_code", length = 10)
    private String verificationCode;

    @Column(name = "verified", columnDefinition = "BIT DEFAULT 0")
    private Boolean verified;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public User(String name, String email, String password, String verificationCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.verified=false;
    }

    public User() {}

}