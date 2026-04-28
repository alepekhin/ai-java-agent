package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Стандартное приложение SpringBoot
 */
@SpringBootApplication
public class DemoApplication {

    /**
     * Стандартная точка входа в Java приложение
     */
	public static void main(String[] args) {
		new SpringApplication(DemoApplication.class).run(args);
	}

}
