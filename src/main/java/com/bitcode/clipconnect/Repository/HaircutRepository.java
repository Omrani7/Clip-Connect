package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Haircut;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HaircutRepository extends JpaRepository<Haircut, Long> {
    List<Haircut> findByBarberId(Long barberId);
}
