package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameOrEmail(String name, String email);
    User findByEmail(String email);
    User findByName(String username);
    Optional<User> findById(Long id);
    boolean existsByName(String username);
    boolean existsByEmail(String username);
}
