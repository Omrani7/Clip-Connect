package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
