package com.bitcode.clipconnect.Controller;
import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.User;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, BarberRepository barberRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.barberRepository = barberRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user, @RequestParam String role){
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        if (role.equals("barber")) {
            Barber barber = new Barber();
            barber.setUser(savedUser);
            barberRepository.save(barber);
        } else if (role.equals("client")) {
            Client client = new Client();
            client.setUser(savedUser);
            clientRepository.save(client);
        }

        return savedUser;
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User loginUser) {
        User user = null;

        try {
            user = userRepository.findByUsername(loginUser.getUsername());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred", e);
        }

        if (user != null && passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}
