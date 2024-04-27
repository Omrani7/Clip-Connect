package com.bitcode.clipconnect.Service;

import com.bitcode.clipconnect.Model.Barber;
import com.bitcode.clipconnect.Model.Location;
import com.bitcode.clipconnect.Model.User;
import com.bitcode.clipconnect.Repository.BarberRepository;
import com.bitcode.clipconnect.Repository.LocationRepository;
import com.bitcode.clipconnect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final BarberRepository barberRepository;

    @Autowired
    public DataInitializationService(UserRepository userRepository, LocationRepository locationRepository, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.barberRepository = barberRepository;
    }

    public void initData() {
        if (userRepository.count() == 0 && locationRepository.count() == 0 && barberRepository.count() == 0) {
            double[][] coordinates = {
                    {36.8065, 10.1815},
                    {34.7397, 10.7606},
                    {35.8254, 10.6362},
                    {33.8869, 9.5375},
                    {36.4625, 10.7528},
                    {33.8922, 10.0982},
                    {35.6745, 10.0996},
                    {36.8833, 10.09},
                    {35.6911, 10.0911},
                    {36.4667, 10.75},
                    {34.4167, 8.8},
                    {35.5453, 11.0401},
                    {37.2763, 9.8739},
                    {36.5, 8.75},
                    {36.8333, 10.1},
                    {34.4333, 8.7833},
                    {35.6167, 10.8},
                    {36.6572, 10.2883},
                    {36.67, 10.5933},
                    {35.8986, 10.5964},
                    {34.7489, 10.759},
                    {35.7833, 10.8},
                    {35.2995, 10.7054},
                    {36.6647, 10.5876},
                    {36.4167, 10.75},
                    {35.7353, 10.5802},
                    {35.5, 11},
                    {33.8833, 10.1167},
                    {37.2692, 9.86},
                    {34.7741, 10.7619},
                    {36.3833, 10.6},
                    {36.8011, 10.1797},
                    {37.2425, 9.8647},
                    {35.6256, 10.835},
                    {35.2333, 11.25},
                    {36.4589, 10.7336},
                    {35.6667, 10.1167},
                    {35.6567, 10.7339},
                    {35.2333, 11.2},
                    {35.7211, 10.6914},
                    {36.8167, 10.1167},
                    {36.8917, 10.2986},
                    {36.5833, 10.4833},
                    {36.6667, 10.5833},
                    {36.878, 10.3242},
                    {36.4156, 10.6117},
                    {36.85, 10.6},
                    {36.7695, 10.1935},
                    {35.6072, 10.9944},
                    {36.4417, 10.75},
                    {36.4667, 10.7667},
                    {36.6333, 10.4333},
                    {36.826, 10.0495}
            };


            for (int i = 0; i < 50; i++) {
                Location location = new Location();
                location.setLatitude(coordinates[i % coordinates.length][0]);
                location.setLongitude(coordinates[i % coordinates.length][1]);
                location = locationRepository.save(location);

                User user = new User();
                user.setName("Barber " + (i + 1));
                user.setEmail("barber" + (i + 1) + "@example.com");
                user.setPassword("password");
                user.setVerified(true);
                user.setLocation(location);
                user = userRepository.save(user);

                Barber barber = new Barber();
                barber.setShopName("Barber Shop " + (i + 1));
                barber.setPhoneNumber("1234567890");
                barber.setUser(user);
                barber.setBio("Experienced barber with 10+ years of experience.");
                barberRepository.save(barber);
            }
        }
    }

}