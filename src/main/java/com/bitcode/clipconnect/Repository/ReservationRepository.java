package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByBarberId(Long barberId);

    boolean existsByClientIdAndBarberId(Long clientIdToCheck, Long barberId);
}
