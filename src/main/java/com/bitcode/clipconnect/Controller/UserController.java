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

import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;
    private final MailSender mailSender;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          BarberRepository barberRepository,
                          ClientRepository clientRepository,
                          MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.barberRepository = barberRepository;
        this.clientRepository = clientRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user, @RequestParam String role) {
        if (userRepository.existsByName(user.getName())) {
            throw new RuntimeException("Username already exists");
        }

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser.getEmail(), verificationCode);

        if ("barber".equals(role)) {
            Barber barber = new Barber();

            barber.setUser(savedUser);
            return barberRepository.save(barber).getUser();
        } else if ("client".equals(role)) {
            Client client = new Client();

            client.setUser(savedUser);
            return clientRepository.save(client).getUser();
        } else {
            throw new RuntimeException("Invalid role");
        }
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User loginUser) {
        User user = userRepository.findByNameOrEmail(loginUser.getName(), loginUser.getName());

        if (user != null && passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/verify")
    public String verifyUser(@RequestParam String email, @RequestParam String verificationCode) {
        User user = userRepository.findByEmail(email);
        if (user != null && verificationCode.equals(user.getVerificationCode())) {
            user.setVerified(true);
            userRepository.save(user);
            return "User verified successfully";
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            userRepository.save(user);
            sendResetPasswordEmail(email, verificationCode);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String email, @RequestParam String verificationCode, @RequestParam String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user != null && verificationCode.equals(user.getVerificationCode())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setVerificationCode(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid reset code");
        }
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
