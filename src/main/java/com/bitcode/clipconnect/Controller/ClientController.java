package com.bitcode.clipconnect.Controller;

import java.util.*;

import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Repository.ClientRepository;
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
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }}