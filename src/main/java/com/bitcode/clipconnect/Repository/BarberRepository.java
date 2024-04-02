package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber,Long> {
}