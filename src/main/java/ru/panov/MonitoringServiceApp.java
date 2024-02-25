package ru.panov;


import annotations.EnableXXX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableXXX
@SpringBootApplication
public class MonitoringServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringServiceApp.class, args);
    }
}