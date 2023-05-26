package com.inmotionchat.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.inmotionchat"})
@EntityScan(basePackages = {"com.inmotionchat"})
public class InMotion {

    public static void main(String[] args) throws Exception {
        InMotion inMotion = new InMotion(args);
    }

    /**
     * Default constructor for Spring startup.
     */
    public InMotion() {}

    public InMotion(String[] args) throws Exception {
        SpringApplication.run(InMotion.class, args);
    }

}
