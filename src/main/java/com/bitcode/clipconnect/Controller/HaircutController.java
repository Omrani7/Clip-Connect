package com.bitcode.clipconnect.Controller;

import com.bitcode.clipconnect.Model.Haircut;
import com.bitcode.clipconnect.Repository.HaircutRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/haircuts")
public class HaircutController {

    @Autowired
    private final HaircutRepository haircutRepository;

    public HaircutController(HaircutRepository haircutRepository) {
        this.haircutRepository = haircutRepository;
    }

    @GetMapping
    public List<Haircut> getAllHaircuts() {
        return haircutRepository.findAll();
    }

    @GetMapping("/{id}")
    public Haircut getHaircutById(@PathVariable Long id) {
        return haircutRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Haircut createHaircut(@RequestBody Haircut haircut) {
        return haircutRepository.save(haircut);
    }

    @PutMapping("/{id}")
    public Haircut updateHaircut(@PathVariable Long id, @RequestBody Haircut haircut) {
        haircut.setId(id); // Set id explicitly in case it's missing in the request body
        return haircutRepository.save(haircut);
    }

    @DeleteMapping("/{id}")
    public void deleteHaircut(@PathVariable Long id) {
        haircutRepository.deleteById(id);
    }
}
