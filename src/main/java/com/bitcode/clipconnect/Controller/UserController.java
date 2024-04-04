package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Client;
import com.bitcode.clipconnect.Model.User;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.ClientRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import com.bitcode.clipconnect.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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
    public Map<String, Object> registerUser(@RequestBody User user, @RequestParam String role) {
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
        //sendVerificationEmail(savedUser.getEmail(), verificationCode);

        if ("barber".equals(role)) {
            Barber barber = new Barber();

            barber.setUser(savedUser);
            response.put("status", "success");
            response.put("data", barberRepository.save(barber).getUser());
        } else if ("client".equals(role)) {
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

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User loginUser) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByNameOrEmail(loginUser.getName(), loginUser.getName());

        if (user != null && passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            response.put("status", "success");
            response.put("data", user);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid username or password");
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
}
