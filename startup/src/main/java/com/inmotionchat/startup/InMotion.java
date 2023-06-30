package com.inmotionchat.startup;

import com.inmotionchat.core.data.SQLRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.inmotionchat"})
@EntityScan(basePackages = {"com.inmotionchat"})
@EnableJpaRepositories(basePackages = {"com.inmotionchat"}, repositoryBaseClass = SQLRepositoryImpl.class)
@EnableRedisRepositories(basePackages = {"com.inmotionchat"})
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
