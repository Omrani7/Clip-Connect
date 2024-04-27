package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.Review;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

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
    public Review reviewBarber(@RequestParam Long clientId, @RequestParam Long barberId, @RequestParam int rating, @RequestParam(required = false) String comment) {
        // Retrieve client and barber entities
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        Barber barber = barberRepository.findById(barberId).orElseThrow(() -> new EntityNotFoundException("Barber not found"));

        // Create and save the review
        Review review = new Review();
        review.setClient(client);
        review.setBarber(barber);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable Long reviewId, @RequestParam(required = false) Integer rating, @RequestParam(required = false) String comment) {
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
        return reviewRepository.save(existingReview);
    }
}
