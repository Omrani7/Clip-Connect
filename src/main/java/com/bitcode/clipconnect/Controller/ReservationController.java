package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Reservation;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Repository.ReservationRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.BarberRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public ReservationController(ReservationRepository reservationRepository, ClientRepository clientRepository, BarberRepository barberRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.barberRepository = barberRepository;
    }

    // Create a new reservation
    @PostMapping("/create")
    public ApiResponse<Reservation> createReservation(
            @RequestParam("userId") Long userId,
            @RequestParam("barberId") Long barberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        try {
            // Retrieve client and barber using userId and barberId
            Optional<Client> clientOpt = Optional.ofNullable(clientRepository.findByUserId(userId));
            Optional<Barber> barberOpt = barberRepository.findById(barberId);

            if (clientOpt.isEmpty() || barberOpt.isEmpty()) {
                return new ApiResponse<>("error", "Client or Barber not found");
            }

            // Create a new reservation
            Reservation reservation = new Reservation();
            reservation.setClient(clientOpt.get());
            reservation.setBarber(barberOpt.get());
            reservation.setDate(date);

            // Save the reservation
            Reservation savedReservation = reservationRepository.save(reservation);

            return new ApiResponse<>("success", "Reservation created successfully", savedReservation);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }


    // Retrieve all reservations
    @GetMapping
    public ApiResponse<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations.isEmpty()) {
                return new ApiResponse<>("success", "No reservations found", null);
            }
            return new ApiResponse<>("success", "Reservations retrieved successfully", reservations);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }

    // Retrieve a single reservation by ID
    @GetMapping("/by-id")
    public ApiResponse<Reservation> getReservationById(@RequestParam Long id) {
        Optional<Reservation> reservationData = reservationRepository.findById(id);
        return reservationData.map(reservation -> new ApiResponse<>("success", "Reservation found", reservation))
                .orElse(new ApiResponse<>("error", "Reservation not found", null));
    }

    // Retrieve reservations by user ID
    @GetMapping("/by-user-id")
    public ApiResponse<List<Reservation>> getReservationsByUserId(@RequestParam Long userId) {
        try {
            Optional<Client> clientOpt = Optional.ofNullable(clientRepository.findByUserId(userId));
            if (clientOpt.isPresent()) {
                List<Reservation> reservations = reservationRepository.findByClientId(clientOpt.get().getId());
                if (reservations.isEmpty()) {
                    return new ApiResponse<>("success", "No reservations found for this user", null);
                }
                return new ApiResponse<>("success", "Reservations retrieved successfully", reservations);
            } else {
                return new ApiResponse<>("error", "Client not found", null);
            }
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }

    // Retrieve reservations by barber ID
    @GetMapping("/by-barber-id")
    public ApiResponse<List<Reservation>> getReservationsByBarberId(@RequestParam Long barberId) {
        try {
            List<Reservation> reservations = reservationRepository.findByBarberId(barberId);
            if (reservations.isEmpty()) {
                return new ApiResponse<>("success", "No reservations found for this barber", null);
            }
            return new ApiResponse<>("success", "Reservations retrieved successfully", reservations);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }

    // Update a reservation by ID
    @PutMapping("/update")
    public ApiResponse<Reservation> updateReservation(@RequestParam Long id, @RequestBody Reservation reservation) {
        Optional<Reservation> reservationData = reservationRepository.findById(id);
        if (reservationData.isPresent()) {
            Reservation existingReservation = reservationData.get();
            existingReservation.setDate(reservation.getDate());
            existingReservation.setExpired(reservation.getExpired());

            Optional<Client> clientOpt = clientRepository.findById(reservation.getClient().getId());
            Optional<Barber> barberOpt = barberRepository.findById(reservation.getBarber().getId());
            if (clientOpt.isEmpty() || barberOpt.isEmpty()) {
                return new ApiResponse<>("error", "Client or Barber not found");
            }
            existingReservation.setClient(clientOpt.get());
            existingReservation.setBarber(barberOpt.get());

            Reservation updatedReservation = reservationRepository.save(existingReservation);
            return new ApiResponse<>("success", "Reservation updated successfully", updatedReservation);
        } else {
            return new ApiResponse<>("error", "Reservation not found", null);
        }
    }

    // Delete a reservation by ID
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteReservation(@RequestParam Long id) {
        try {
            reservationRepository.deleteById(id);
            return new ApiResponse<>("success", "Reservation deleted successfully", null);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }

    // Delete all reservations
    @DeleteMapping
    public ApiResponse<String> deleteAllReservations() {
        try {
            reservationRepository.deleteAll();
            return new ApiResponse<>("success", "All reservations deleted successfully", null);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", null);
        }
    }
    @GetMapping("/check-client-review")
    public ApiResponse<Boolean> checkClientReview(@RequestParam Long userId, @RequestParam Long barberId) {
        try {
            Optional<Client> clientOpt = Optional.ofNullable(clientRepository.findByUserId(userId));
            if (clientOpt.isPresent()) {
                Long clientIdToCheck = clientOpt.get().getId();
                boolean hasReviewed = reservationRepository.existsByClientIdAndBarberId(clientIdToCheck, barberId);
                return new ApiResponse<>("success", "Client review checked", hasReviewed);
            } else {
                return new ApiResponse<>("error", "Client not found", false);
            }
        } catch (Exception e) {
            return new ApiResponse<>("error", "Internal Server Error", false);
        }
    }
}
