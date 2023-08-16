package com.inmotionchat.startup;

import com.inmotionchat.smartpersist.SmartJPARepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.List;

import static com.inmotionchat.core.util.misc.ServiceResolution.artifact;

@SpringBootApplication
@ComponentScan(basePackages = "${basePackages}")
@EntityScan(basePackages = {"com.inmotionchat"})
@EnableJpaRepositories(basePackages = {"com.inmotionchat"}, repositoryBaseClass = SmartJPARepositoryImpl.class)
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
        List<String> serviceClassNames = InMotionConfiguration.getInstance().getServicesToAwaken();

        String basePackages = "com.inmotionchat.core";
        basePackages = "com.inmotionchat.startup," + basePackages;

        for (String className : serviceClassNames) {
            basePackages = "com.inmotionchat." + artifact(Class.forName(className)) + "," + basePackages;
        }

        System.setProperty("basePackages", basePackages);

        SpringApplication.run(InMotion.class, args);
    }

}
