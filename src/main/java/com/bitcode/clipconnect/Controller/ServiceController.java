package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Service;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final BarberRepository barberRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, BarberRepository barberRepository) {
        this.serviceRepository = serviceRepository;
        this.barberRepository = barberRepository;
    }

    // Get all services

    // Get services by barber_id
    @GetMapping("/by-barber-id")
    public ResponseEntity<ApiResponse<List<Service>>> getServicesByBarberId(@RequestParam Long barberId) {
        List<Service> services = serviceRepository.findByBarberId(barberId);
        ApiResponse<List<Service>> response = new ApiResponse<>("success", "Services retrieved by barber ID successfully", services);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Create a new service
    @PostMapping
    public ResponseEntity<ApiResponse<Service>> createService(@RequestBody Service service, @RequestParam Long barberId) {
        Barber existingBarber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Barber ID: " + barberId));
        service.setBarber(existingBarber);
        Service createdService = serviceRepository.save(service);
        ApiResponse<Service> response = new ApiResponse<>("success", "Service created successfully", createdService);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update an existing service
    @PutMapping
    public ResponseEntity<ApiResponse<Service>> updateService(@RequestParam Long id, @RequestBody Service service) {
        Optional<Service> existingServiceOpt = serviceRepository.findById(id);
        if (existingServiceOpt.isEmpty()) {
            ApiResponse<Service> response = new ApiResponse<>("error", "Service not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Service existingService = existingServiceOpt.get();
        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setPrice(service.getPrice());
        Service updatedService = serviceRepository.save(existingService);
        ApiResponse<Service> response = new ApiResponse<>("success", "Service updated successfully", updatedService);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete a service
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteService(@RequestParam Long id) {
        if (!serviceRepository.existsById(id)) {
            ApiResponse<Void> response = new ApiResponse<>("error", "Service not found");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        serviceRepository.deleteById(id);
        ApiResponse<Void> response = new ApiResponse<>("success", "Service deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
