package org.beigesoft.bservice1;

import org.beigesoft.busn.mdl.BnkPaymJsn;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BServ1BnkCnsm {

  private BService1Srv bService1Srv;

  public BServ1BnkCnsm(final BService1Srv pBnkPaymJsnSrv) {
    this.bService1Srv = pBnkPaymJsnSrv;
  }

  @Bean
  public Consumer<BnkPaymJsn> handlePayment() {
    return (bpayj) -> {
      try {
        this.bService1Srv.mkPayment(bpayj);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
  }
}
