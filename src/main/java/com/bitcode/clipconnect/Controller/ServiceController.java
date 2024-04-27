package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Service;
import com.bitcode.clipconnect.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get a service by ID
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return serviceRepository.findById(id)
                .map(service -> new ResponseEntity<>(service, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get services by barber_id
    @GetMapping("/byBarber/{barberId}")
    public ResponseEntity<List<Service>> getServicesByBarberId(@PathVariable Long barberId) {
        List<Service> services = serviceRepository.findByBarberId(barberId);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Create a new service
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service createdService = serviceRepository.save(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    // Update an existing service
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        if (!serviceRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.setId(id); // Ensure the correct ID is set
        Service updatedService = serviceRepository.save(service);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    // Delete a service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (!serviceRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        serviceRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
