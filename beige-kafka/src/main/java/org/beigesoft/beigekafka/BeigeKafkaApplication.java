package org.beigesoft.beigekafka;

import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages={"org.beigesoft"})
public class BeigeKafkaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeigeKafkaApplication.class, args);
  }
}
