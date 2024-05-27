package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Location;
import com.bitcode.clipconnect.Model.User;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/barbers")
public class BarberController {

    @Autowired
    private BarberRepository barberRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/{barberId}")
    public ResponseEntity<ApiResponse<Barber>> getBarberById(@PathVariable Long barberId) {
        try {
            // Retrieve the barber by ID from the repository
            Optional<Barber> optionalBarber = barberRepository.findById(barberId);
            if (optionalBarber.isEmpty()) {
                // If the barber is not found, return a not found response
                ApiResponse<Barber> response = new ApiResponse<>("error", "Barber not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // If the barber is found, return a success response with the barber data
            Barber barber = optionalBarber.get();
            ApiResponse<Barber> response = new ApiResponse<>("success", "Barber retrieved successfully", barber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If an error occurs, return an internal server error response
            ApiResponse<Barber> response = new ApiResponse<>("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PutMapping("/busy-switch")
    public ResponseEntity<ApiResponse<Barber>> toggleBarberBusyStatus(@RequestParam Long id) {
        try {
            Optional<Barber> optionalBarber = barberRepository.findById(id);
            if (optionalBarber.isEmpty()) {
                ApiResponse<Barber> response = new ApiResponse<>("error", "Barber not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Barber barber = optionalBarber.get();
            // Toggle the busy status
            barber.setBusy(!barber.getBusy());
            barberRepository.save(barber);

            ApiResponse<Barber> response = new ApiResponse<>("success", "Barber busy status toggled successfully", barber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Barber> response = new ApiResponse<>("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-busy")
    public ResponseEntity<ApiResponse<Boolean>> getBarberBusyStatus(@RequestParam Long id) {
        try {
            Optional<Barber> optionalBarber = barberRepository.findById(id);
            if (optionalBarber.isEmpty()) {
                ApiResponse<Boolean> response = new ApiResponse<>("error", "Barber not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Barber barber = optionalBarber.get();
            ApiResponse<Boolean> response = new ApiResponse<>("success", "Barber busy status retrieved successfully", barber.getBusy());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Boolean> response = new ApiResponse<>("error", "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllBarbers() {
        try {
            List<Barber> barbers = barberRepository.findAll();
            List<Map<String, Object>> barbersList = new ArrayList<>();
            for (Barber barber : barbers) {
                Map<String, Object> barberData = new HashMap<>();
                barberData.put("id", barber.getId());
                barberData.put("shopName", barber.getShopName());
                barberData.put("phoneNumber", barber.getPhoneNumber());
                barberData.put("bio", barber.getBio());
                barberData.put("user",barber.getUser());
                barberData.put("isSet", barber.getIsSet());
                barberData.put("location", barber.getUser().getLocation());
                barbersList.add(barberData);
            }

            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>("success", "Barbers retrieved successfully", barbersList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/setup")
    public ResponseEntity<ApiResponse<Barber>> updateBarber(@RequestParam Long id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<Barber> optionalBarber = barberRepository.findById(id);
            if (optionalBarber.isEmpty()) {
                ApiResponse<Barber> response = new ApiResponse<>("error", "Barber not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Barber barber = optionalBarber.get();

            // Update barber information
            if (updates.containsKey("shopName")) {
                barber.setShopName((String) updates.get("shopName"));
            }
            if (updates.containsKey("avatarUrl")) {
                barber.getUser().setAvatarUrl((String) updates.get("avatarUrl"));
            }
            if (updates.containsKey("phoneNumber")) {
                barber.setPhoneNumber((String) updates.get("phoneNumber"));
            }
            if (updates.containsKey("bio")) {
                barber.setBio((String) updates.get("bio"));
            }
            if (updates.containsKey("location")) {
                Map<String, Object> locationData = (Map<String, Object>) updates.get("location");
                Location location = barber.getUser().getLocation();
                if (location == null) {
                    location = new Location();
                }
                if (locationData.containsKey("latitude")) {
                    location.setLatitude((Double) locationData.get("latitude"));
                }
                if (locationData.containsKey("longitude")) {
                    location.setLongitude((Double) locationData.get("longitude"));
                }
                barber.getUser().setLocation(location);
            }
            barber.setIsSet(true);
            barberRepository.save(barber);
            Optional<User> user = userRepository.findById(barber.getUser().getId());
            ApiResponse<Barber> response = new ApiResponse<>("success", "Barber updated successfully", barber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Barber> response = new ApiResponse<>("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
