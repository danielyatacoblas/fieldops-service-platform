package com.portfolio.fieldops;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FieldOpsApplication {
    public static void main(String[] args) { SpringApplication.run(FieldOpsApplication.class, args); }
    @Bean Clock systemClock() { return Clock.systemUTC(); }
}
