package org.beigesoft.busn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTransactionManagement
public class BeigeBusnApplication {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  public static void main(final String[] args) {
    SpringApplication.run(BeigeBusnApplication.class, args);
  }

  @Bean
  public CommandLineRunner populDb(final TstEagerQuSrv pTstEagerQuSrv) {
    return (args) -> {
      for (String arg : args) {
        if (arg.contains("populDb")) {
          this.logger.info(
    "Try to populate DB with sample data. It must be in single transaction!!!");
          pTstEagerQuSrv.populDb();
          //transaction OK!
          return;
        }
      }
      this.logger.info("There is no command to populate DB with sample data.");
    };
  }
}
