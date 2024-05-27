package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BarberRepository extends JpaRepository<Barber,Long> {
    Barber findByUserId(Long userId);
}