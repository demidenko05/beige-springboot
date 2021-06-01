package org.beigesoft.busn;

//import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableTransactionManagement
public class BeigeBusnApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeigeBusnApplication.class, args);
  }
}
