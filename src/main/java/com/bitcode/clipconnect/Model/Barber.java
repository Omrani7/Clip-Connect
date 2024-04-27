package com.bitcode.clipconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_name", length = 100)
    private String shopName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bio")
    private String bio;

    public Barber(){

    }

}
