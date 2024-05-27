package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.Location;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public ClientController(ClientRepository clientRepository, BarberRepository barberRepository) {
        this.clientRepository = clientRepository;
        this.barberRepository = barberRepository;
    }

    // New endpoint to get recommendation for nearest barber
    @GetMapping("/recommendation")
    public ResponseEntity<ApiResponse<List<Barber>>> getRecommendation(@RequestParam Long userId) {
        try {
            Client client = clientRepository.findByUserId(userId);

            if (client == null || client.getUser().getLocation() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error", "Client not found or location not available."));
            }

            List<Barber> allBarbers = barberRepository.findAll();

            // Filter out barbers without a location
            List<Barber> barbersWithLocation = allBarbers.stream()
                    .filter(barber -> barber.getUser().getLocation() != null)
                    .collect(Collectors.toList());

            if (barbersWithLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error", "No barbers available."));
            }

            List<Barber> nearestBarbers = findNearestBarbers(client.getUser().getLocation(), barbersWithLocation, 4);

            if (nearestBarbers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error", "No nearby barbers found."));
            }

            return ResponseEntity.ok(new ApiResponse<>("success", "Nearest barbers retrieved successfully", nearestBarbers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage()));
        }
    }


    private List<Barber> findNearestBarbers(Location clientLocation, List<Barber> barbers, int numBarbers) {
        // Create a priority queue to store the nearest barbers based on distance
        PriorityQueue<Barber> nearestBarbersQueue = new PriorityQueue<>(Comparator.comparingDouble(barber -> calculateDistance(clientLocation, barber.getUser().getLocation())));

        // Add eligible barbers (within 7 km) to the priority queue
        for (Barber barber : barbers) {
            double distance = calculateDistance(clientLocation, barber.getUser().getLocation());
            if (distance <= 7.0) {
                nearestBarbersQueue.add(barber);
            }
        }

        // Retrieve the top numBarbers nearest barbers from the priority queue
        List<Barber> nearestBarbers = new ArrayList<>();
        for (int i = 0; i < numBarbers && !nearestBarbersQueue.isEmpty(); i++) {
            nearestBarbers.add(nearestBarbersQueue.poll());
        }

        return nearestBarbers;
    }

    private double calculateDistance(Location location1, Location location2) {
        final int R = 6371;

        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ApiResponse<Client>> getClientById(@PathVariable Long clientId) {
        try {
            // Retrieve the client by ID from the repository
            Client client = clientRepository.findById(clientId).orElse(null);

            if (client == null) {
                // If the client is not found, return a not found response
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error", "Client not found."));
            }

            // If the client is found, return a success response with the client data
            return ResponseEntity.ok(new ApiResponse<>("success", "Client retrieved successfully", client));
        } catch (Exception e) {
            // If an error occurs, return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage()));
        }
    }
}

