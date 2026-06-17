package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основное приложение Spring Boot, которое запускает процесс обработки файлов и отправки запросов в чат-клиент.
 */
@SpringBootApplication
public class DemoApplication {

    /**
     * Точка входа в Java приложение.
     *
     * @param args аргументы командной строки
     */
	public static void main(String[] args) {
		new SpringApplication(DemoApplication.class).run(args);
	}

}

