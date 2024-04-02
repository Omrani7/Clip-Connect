package com.bitcode.clipconnect.Repository;

import com.bitcode.clipconnect.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // Updated parameter name to 'username'
}
