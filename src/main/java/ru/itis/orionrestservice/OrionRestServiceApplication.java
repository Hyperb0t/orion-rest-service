package ru.itis.orionrestservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("ru.itis.orionrestservice.repositories")
@EnableAspectJAutoProxy
@EnableScheduling
@Slf4j
public class OrionRestServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(OrionRestServiceApplication.class, args);
    }

}

