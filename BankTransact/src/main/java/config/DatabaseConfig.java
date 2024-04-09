package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.anbel.BankTransact.repository")
@Configuration
public class DatabaseConfig {
}