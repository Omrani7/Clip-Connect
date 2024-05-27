package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findById(Long id);
    Client findByUserId(Long userId);

}
