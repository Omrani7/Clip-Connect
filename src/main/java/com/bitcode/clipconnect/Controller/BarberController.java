package com.bitcode.clipconnect.Controller;

import java.util.*;
import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Repository.BarberRepository;
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