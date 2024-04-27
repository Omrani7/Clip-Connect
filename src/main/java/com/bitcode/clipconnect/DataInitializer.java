package com.bitcode.clipconnect;

import com.bitcode.clipconnect.Service.DataInitializationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final DataInitializationService dataInitializationService;

    public DataInitializer(DataInitializationService dataInitializationService) {
        this.dataInitializationService = dataInitializationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataInitializationService.initData();
    }
}
