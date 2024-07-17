package com.api.fundtransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point of the Fund Transfer Application.
 *
 * <p>This class initializes the Spring Boot application and enables Feign clients for external API communication.
 * </p>
 *
 * @Author Rajeev Verma
 */
@SpringBootApplication
@EnableFeignClients
public class FundtransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundtransferApplication.class, args);
    }

}
