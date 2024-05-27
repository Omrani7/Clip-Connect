package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Haircut;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.HaircutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/haircuts")
public class HaircutController {

    private final HaircutRepository haircutRepository;
    private final BarberRepository barberRepository;
    @Autowired
    public HaircutController(HaircutRepository haircutRepository, BarberRepository barberRepository) {
        this.haircutRepository = haircutRepository;
        this.barberRepository = barberRepository;
    }

    @GetMapping("/by-barber-id")
    public ResponseEntity<ApiResponse<List<Haircut>>> getHaircutsByBarberId(@RequestParam Long barberId) {
        List<Haircut> haircuts = haircutRepository.findByBarberId(barberId);
        ApiResponse<List<Haircut>> response = new ApiResponse<>("success", "Haircuts retrieved successfully for barber ID: " + barberId, haircuts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Haircut>> createHaircut(@RequestBody Haircut haircut, @RequestParam Long barberId) {
        // Fetch the Barber entity from the database
        Barber existingBarber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Barber ID: " + barberId));

        // Set the fetched Barber to the Haircut
        haircut.setBarber(existingBarber);

        // Save the Haircut entity
        Haircut createdHaircut = haircutRepository.save(haircut);
        ApiResponse<Haircut> response = new ApiResponse<>("success", "Haircut created successfully", createdHaircut);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Haircut>> updateHaircut(@RequestParam Long id, @RequestBody Haircut haircut) {
        Optional<Haircut> existingHaircutOpt = haircutRepository.findById(id);
        if (existingHaircutOpt.isEmpty()) {
            ApiResponse<Haircut> response = new ApiResponse<>("error", "Haircut not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Haircut existingHaircut = existingHaircutOpt.get();
        existingHaircut.setName(haircut.getName());
        existingHaircut.setDescription(haircut.getDescription());
        existingHaircut.setImageUrl(haircut.getImageUrl());
        Haircut updatedHaircut = haircutRepository.save(existingHaircut);
        ApiResponse<Haircut> response = new ApiResponse<>("success", "Haircut updated successfully", updatedHaircut);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteHaircut(@RequestParam Long id) {
        if (haircutRepository.existsById(id)) {
            haircutRepository.deleteById(id);
            ApiResponse<Void> response = new ApiResponse<>("success", "Haircut deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<Void> response = new ApiResponse<>("error", "Haircut not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}

