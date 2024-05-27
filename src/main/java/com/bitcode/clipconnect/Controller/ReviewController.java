package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.Review;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public ReviewController(ReviewRepository reviewRepository, ClientRepository clientRepository, BarberRepository barberRepository) {
        this.reviewRepository = reviewRepository;
        this.clientRepository = clientRepository;
        this.barberRepository = barberRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> reviewBarber(@RequestParam Long clientId, @RequestParam Long barberId, @RequestParam int rating, @RequestParam(required = false) String comment) {
        try {
            // Retrieve client and barber entities
            Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
            Barber barber = barberRepository.findById(barberId).orElseThrow(() -> new EntityNotFoundException("Barber not found"));

            // Create and save the review
            Review review = new Review();
            review.setClient(client);
            review.setBarber(barber);
            review.setRating(rating);
            review.setComment(comment);
            Review savedReview = reviewRepository.save(review);

            ApiResponse<Review> response = new ApiResponse<>("success", "Review created successfully", savedReview);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (EntityNotFoundException ex) {
            ApiResponse<Review> response = new ApiResponse<>("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Review>> updateReview(@RequestParam Long reviewId, @RequestParam(required = false) Integer rating, @RequestParam(required = false) String comment) {
        try {
            // Retrieve the review to update
            Review existingReview = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));

            // Update fields if provided
            if (rating != null) {
                existingReview.setRating(rating);
            }
            if (comment != null) {
                existingReview.setComment(comment);
            }

            // Save the updated review
            Review updatedReview = reviewRepository.save(existingReview);
            ApiResponse<Review> response = new ApiResponse<>("success", "Review updated successfully", updatedReview);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            ApiResponse<Review> response = new ApiResponse<>("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-barber")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByBarberId(@RequestParam Long barberId) {
        try {
            List<Review> reviews = reviewRepository.findByBarberId(barberId);
            ApiResponse<List<Review>> response = new ApiResponse<>("success", "Reviews retrieved successfully", reviews);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            ApiResponse<List<Review>> response = new ApiResponse<>("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-client")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByClientId(@RequestParam Long userId) {
        try {
            Client client = clientRepository.findByUserId(userId);
            List<Review> reviews = reviewRepository.findByClientId(client.getId());
            ApiResponse<List<Review>> response = new ApiResponse<>("success", "Reviews retrieved successfully", reviews);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            ApiResponse<List<Review>> response = new ApiResponse<>("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-barber-client")
    public ResponseEntity<ApiResponse<Review>> getReviewByBarberAndClientId(@RequestParam Long barberId, @RequestParam Long clientId) {
        try {
            Optional<Review> reviewOptional = reviewRepository.findByBarberIdAndClientId(barberId, clientId);
            if (reviewOptional.isPresent()) {
                Review review = reviewOptional.get();
                ApiResponse<Review> response = new ApiResponse<>("success", "Review retrieved successfully", review);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                throw new EntityNotFoundException("Review not found for the given barber and client");
            }
        } catch (EntityNotFoundException e) {
            ApiResponse<Review> response = new ApiResponse<>("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteReview(@RequestParam Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            ApiResponse<Void> response = new ApiResponse<>("success", "Review deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Void> response = new ApiResponse<>("error", "Review not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
