package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBarberId(Long barberId);

    List<Review> findByClientId(Long clientId);

    Optional<Review> findByBarberIdAndClientId(Long barberId, Long clientId);
}
