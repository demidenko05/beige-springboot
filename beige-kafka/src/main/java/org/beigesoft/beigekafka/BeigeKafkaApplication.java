package org.beigesoft.beigekafka;

//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

@SpringBootApplication(exclude={JdbcTemplateAutoConfiguration.class})
//@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages={"org.beigesoft"})
public class BeigeKafkaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeigeKafkaApplication.class, args);
  }
}
