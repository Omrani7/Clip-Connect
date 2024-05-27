package com.bitcode.clipconnect.Service;

import com.bitcode.clipconnect.Model.*;
import com.bitcode.clipconnect.Repository.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Service
public class DataInitializationService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;
    private final HaircutRepository haircutRepository;
    private final EntityManager entityManager;

    @Autowired
    public DataInitializationService(UserRepository userRepository, LocationRepository locationRepository, BarberRepository barberRepository, ServiceRepository serviceRepository, HaircutRepository haircutRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
        this.haircutRepository = haircutRepository;
        this.entityManager = entityManager;
    }
    @Transactional
    public void initData() {
        Random random = new Random();
        if (userRepository.count() == 0 && locationRepository.count() == 0 && barberRepository.count() == 0) {
            double[][] coordinates = {
                    {35.768992, 10.818182},
                    {35.767600, 10.814955},
                    {35.766587, 10.813225},
                    {35.765101, 10.809951},
                    {35.768023, 10.821503},
                    {35.765159, 10.825693},
                    {35.762042, 10.826546},
                    {35.758328, 10.826158},
                    {35.757930, 10.825781},
                    {35.749114, 10.824519},
                    {35.757663, 10.833790},
                    {35.766714, 10.813019},
                    {35.775087, 10.814935},
                    {35.778195, 10.815675},
                    {35.772967, 10.802613},
                    {35.774592, 10.796212},
                    {35.773992, 10.792815},
                    {35.775935, 10.823339},
                    {35.770635, 10.828346},
                    {35.767456, 10.827127},
                    {35.766360, 10.831264},
                    {35.768198, 10.836402},
                    {35.764735, 10.836968},
                    {35.758935, 10.822576},
            };

            List<String> barbershopNames = Arrays.asList(
                    "The Gentleman's Cut",
                    "Clipper Cove",
                    "Razor Sharp Barbers",
                    "Classic Cuts Barbershop",
                    "Shear Excellence",
                    "The Mane Attraction",
                    "Precision Barber Lounge",
                    "Urban Edge Barbers",
                    "The Fade Factory",
                    "Dapper Den Barbershop",
                    "Clip Joint Barbers",
                    "The Chop Shop",
                    "Blade & Bow Barbers",
                    "Crown & Comb Barbershop",
                    "Elite Edge Barbers",
                    "The Grooming Gallery",
                    "The Buzz Barbershop",
                    "Swift Shears Barbers",
                    "The Style Station",
                    "Trim Time Barbers",
                    "Sharp & Suave Barbershop",
                    "Shear Genius Barbers",
                    "Mane Street Barbershop"
            );

            List<String> barberShopNames = Arrays.asList(
                    "Al-Halaaq",
                    "Mashat-Al-Qas",
                    "Halqat-Al-Dhukoor",
                    "Salon-Qasat-Al-Kilasah",
                    "Tamyiz-Al-Halqah",
                    "Jadhbah-Al-Shu'ur",
                    "Salon-Al-Halqah",
                    "Halqat-Al-Hafa",
                    "Masna'-Al-Talashi",
                    "Salon-Al-Halqah",
                    "Halqat-Musharakah",
                    "Matajr-Al-Halqah",
                    "Halqat-Wa-Qaws",
                    "Salon-Wa-Mushat",
                    "Halqat-Hafa",
                    "Salon-Tasfiyf-Al-Shu'ur",
                    "Salon-Al-Halqah",
                    "Halqat-Al-Sari'",
                    "Salon-Al-Usul",
                    "Salon-Tasfiyf",
                    "Halqat-Sharb",
                    "Halqat-Ilm-Al-Usul",
                    "Salon-Mishwar"
            );

            for (int i = 0; i < 23; i++) {
                Location location = new Location();
                location.setLatitude(coordinates[i % coordinates.length][0]);
                location.setLongitude(coordinates[i % coordinates.length][1]);
                entityManager.persist(location);
                location = locationRepository.save(location);

                User user = new User();
                user.setName(barberShopNames.get(i));
                user.setEmail(barberShopNames.get(i) + "@example.com");
                user.setPassword("password");
                user.setVerified(true);
                user.setAvatarUrl("http://localhost:8080/api/uploads/image/avatar_" + (random.nextInt(5) + 1) + ".jpeg");
                user.setLocation(location);
                user.setRole(UserRole.BARBER);
                user = userRepository.save(user);

                Barber barber = new Barber();
                barber.setShopName(barbershopNames.get(i));
                barber.setPhoneNumber("14836932");
                barber.setUser(user);
                barber.setBio("As a dedicated barber, I take pride in delivering exceptional service and quality craftsmanship. Whether you're due for a trim or a complete transformation, I've got you covered.");
                barberRepository.save(barber);
                for (int j = 0; j < 3; j++) {
                    Service service = new Service();
                    service.setBarber(barber);
                    service.setName("Random name " + j);
                    service.setPrice(random.nextInt(50) + 10); // Random price between 10 and 59
                    service.setDescription("Random service description " + j);
                    service.setImageUrl("http://localhost:8080/api/uploads/image/serve.jpg");
                    serviceRepository.save(service);

                }

                for (int j = 0; j < 3; j++) {
                    Haircut haircut = new Haircut();
                    haircut.setBarber(barber);
                    haircut.setName("Random name " + j);
                    haircut.setDescription("Random haircut description " + j);
                    haircut.setImageUrl("http://localhost:8080/api/uploads/image/haircut_cj_" + (random.nextInt(6) + 1) + ".jpg");
                    haircutRepository.save(haircut);
                }
            }
        }
    }

}