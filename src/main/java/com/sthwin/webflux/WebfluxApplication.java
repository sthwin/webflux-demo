package com.sthwin.webflux;

import com.sthwin.webflux.config.db.MyBatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebfluxApplication {

    @Autowired
    MyBatisConfig myBatisConfig;

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                System.out.println("myBatisConfig is " + myBatisConfig);
            }
        };
    };
}
