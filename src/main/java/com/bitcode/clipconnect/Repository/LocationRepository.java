package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {}
