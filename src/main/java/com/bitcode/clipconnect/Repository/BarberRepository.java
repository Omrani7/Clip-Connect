package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BarberRepository extends JpaRepository<Barber,Long> {
}