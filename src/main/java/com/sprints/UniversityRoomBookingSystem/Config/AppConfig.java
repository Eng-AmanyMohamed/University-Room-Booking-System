package com.sprints.UniversityRoomBookingSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

public class AppConfig {
    @Configuration
    public class JpaConfig {
        @Bean
        public Clock clock() {
            return Clock.systemUTC();
        }
    }

}
