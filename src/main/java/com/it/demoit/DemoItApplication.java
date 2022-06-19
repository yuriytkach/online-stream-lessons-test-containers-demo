package com.it.demoit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DemoItApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoItApplication.class, args);
  }

}
