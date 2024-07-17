package com.api.fundtransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
@EnableAspectJAutoProxy
public class FundtransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundtransferApplication.class, args);
    }

}
