package com.example.employee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!test-postgres")
@EnableMongoRepositories(basePackages = "com.example.employee.repository")
public class MongoConfig {
    // MongoDB repository configuration - disabled for test-postgres profile
}
