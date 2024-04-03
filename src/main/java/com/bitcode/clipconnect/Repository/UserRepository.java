package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameOrEmail(String name, String email);
    User findByName(String username);
    User findByEmail(String email);
    boolean existsByName(String username);
    boolean existsByEmail(String username);
}
