package com.crm;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@EnableScheduling
public class SalesAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesAutomationApplication.class, args);
    }

    @Bean
    public OpenAPI openApiSwaggerConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("Sales-Automation APIs")
                                .description("Sales-Automation API helps automate sales process by tracking leads and making sure you never miss any.")
                )
                .servers(List.of(new Server().url("http://localhost:8765").description("local")));
    }
}
