package com.bitcode.clipconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bitcode")
public class ClipconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClipconnectApplication.class, args);
	}
}