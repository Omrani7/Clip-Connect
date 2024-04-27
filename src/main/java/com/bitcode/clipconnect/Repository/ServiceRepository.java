package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBarberId(Long barberId);
}
