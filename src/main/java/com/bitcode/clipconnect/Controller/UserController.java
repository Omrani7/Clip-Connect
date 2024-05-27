package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.*;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import com.bitcode.clipconnect.Service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          BarberRepository barberRepository,
                          ClientRepository clientRepository,
                          MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.barberRepository = barberRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registerUser(@RequestBody User user, @RequestParam UserRole role) {
        Map<String, Object> response = new HashMap<>();
        if (userRepository.existsByName(user.getName())) {
            response.put("status", "error");
            response.put("message", "Username already exists");
            return response;
        }

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser.getEmail(), verificationCode);

        if (role == UserRole.BARBER) {
            Barber barber = new Barber();

            barber.setUser(savedUser);
            response.put("status", "success");
            response.put("data", barberRepository.save(barber).getUser());
        } else if (role == UserRole.CLIENT) {
            Client client = new Client();

            client.setUser(savedUser);
            response.put("status", "success");
            response.put("data", clientRepository.save(client).getUser());
        } else {
            response.put("status", "error");
            response.put("message", "Invalid role");
        }
        return response;
    }


    @Transactional
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User loginUser) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByNameOrEmail(loginUser.getName(), loginUser.getEmail());

        if (user == null) {
            response.put("status", "error");
            response.put("message", "Invalid username or email");
        } else if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            response.put("status", "error");
            response.put("message", "Invalid password");
        } else {
            if (!user.getVerified()) {
                resendVerificationCode(user.getEmail(), 0);
            }
            if(user.getRole()==UserRole.CLIENT){
                Client client = clientRepository.findByUserId(user.getId());
                response.put("data", client);
            }else{
                Barber barber = barberRepository.findByUserId(user.getId());
                response.put("data", barber);
            }
            response.put("status", "success");
            System.out.println(user.getId());
        }
        return response;
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyUser(@RequestParam String email, @RequestParam String verificationCode) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email);
        if (user != null && verificationCode.equals(user.getVerificationCode())) {
            user.setVerified(true);
            userRepository.save(user);
            response.put("status", "success");
            response.put("data", "User verified successfully");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid verification code");
        }
        return response;
    }

    @PostMapping("/resend-verification-code")
    public Map<String, Object> resendVerificationCode(@RequestParam String email,@RequestParam int type) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            userRepository.save(user);
            if(type==0){
                sendVerificationEmail(email, verificationCode);
            }
            else if(type==1){
                sendResetPasswordEmail(email, verificationCode);
            }

            response.put("status", "success");
            response.put("data", "Verification code resent successfully");
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
        }
        return response;
    }

    @PostMapping("/forgot-password")
    public Map<String, Object> forgotPassword(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            userRepository.save(user);
            sendResetPasswordEmail(email, verificationCode);
            response.put("status", "success");
            response.put("data", "Reset password email sent successfully");
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
        }
        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestParam String email, @RequestParam String verificationCode, @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email);

        if (user != null && verificationCode.equals(user.getVerificationCode())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setVerificationCode(null);
            userRepository.save(user);
            response.put("status", "success");
            response.put("data", "Password reset successfully");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid reset code");
        }
        return response;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Verify your account";
        String text="Your verification code is: " + verificationCode;
        emailService.sendEmail(email,subject,text);
    }

    private void sendResetPasswordEmail(String email, String resetCode) {
        String subject ="Reset Password";
        String text="Your reset code is: " + resetCode;
        emailService.sendEmail(email,subject,text);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam Long userId, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(updatedUser.getAvatarUrl());
        }

        // Save the updated user
        userRepository.save(existingUser);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", "User updated successfully");

        return ResponseEntity.ok(response);
    }
    @PutMapping("/updateLocation")
    public ResponseEntity<?> updateLocation(@RequestParam Long userId, @RequestBody Location location) {
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get the user's current location
        Location userLocation = existingUser.getLocation();

        if (userLocation != null) {
            // Update the existing location object
            userLocation.setLatitude(location.getLatitude());
            userLocation.setLongitude(location.getLongitude());
        } else {
            // Create a new Location entity and associate it with the user
            Location newLocation = new Location();
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            existingUser.setLocation(newLocation); // Associate location with the user
        }

        // Save the user (which will also cascade to save the location if necessary)
        userRepository.save(existingUser);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User location updated successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/location")
    public ResponseEntity<?> getLocationByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Location location = user.getLocation();
        if (location == null) {
            return ResponseEntity.notFound().build(); // No location found for the user
        }

        return ResponseEntity.ok(location);
    }
}
