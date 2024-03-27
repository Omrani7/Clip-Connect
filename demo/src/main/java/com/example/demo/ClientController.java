package com.example.demo;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientrRepository) {
        this.clientRepository = clientrRepository;
    }

    @GetMapping
    public List<Client> getAllBarbers() {
        return clientRepository.findAll();
    }}