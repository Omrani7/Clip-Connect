package com.example.demo;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/barbers")
public class BarberController {
    private final BarberRepository barberRepository;

    public BarberController(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    @GetMapping
    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }


}