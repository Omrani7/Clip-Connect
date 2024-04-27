package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Report;
import com.bitcode.clipconnect.Model.User;
import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Repository.ReportRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import com.bitcode.clipconnect.Repository.BarberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarberRepository barberRepository;

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        // Retrieve user and barber entities
        User user = userRepository.findById(report.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Barber barber = barberRepository.findById(report.getBarber().getId()).orElseThrow(() -> new EntityNotFoundException("Barber not found"));

        // Set user and barber references in the report object
        report.setUser(user);
        report.setBarber(barber);

        // Save the report
        Report savedReport = reportRepository.save(report);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }
}
