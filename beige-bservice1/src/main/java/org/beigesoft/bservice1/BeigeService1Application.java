package org.beigesoft.bservice1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"org.beigesoft"})
public class BeigeService1Application {

  public static void main(final String[] args) {
    SpringApplication.run(BeigeService1Application.class, args);
  }
}
