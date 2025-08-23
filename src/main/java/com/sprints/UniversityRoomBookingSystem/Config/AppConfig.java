package com.sprints.UniversityRoomBookingSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration

public class AppConfig {
    public class JpaConfig {
        @Bean
        public Clock clock() {
            return Clock.systemUTC();
        }
    }

}
