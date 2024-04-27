package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Repository.BarberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Repository.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/barbers")
public class BarberController {

    @Autowired
    private BarberRepository barberRepository;

    @GetMapping
    public Map<String, Object> getAllBarbers() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> barbersList = barberRepository.findAll().stream()
                .map(barber -> {
                    return new HashMap<String, Object>() {{
                        put("barber", barber);
                    }};
                })
                .collect(Collectors.toList());

        response.put("status", "success");
        response.put("data", barbersList);

        return response;
    }
}
