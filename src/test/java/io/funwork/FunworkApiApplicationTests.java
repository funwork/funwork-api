package io.funwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.funwork.api.FunworkApiApplication;

@SpringBootApplication
@EnableConfigurationProperties
public class FunworkApiApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(FunworkApiApplication.class, args);
    }

}
