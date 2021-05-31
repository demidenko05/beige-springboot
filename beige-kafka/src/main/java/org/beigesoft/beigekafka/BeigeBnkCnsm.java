package org.beigesoft.beigekafka;

import org.beigesoft.busn.mdl.BnkPaymJsn;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeigeBnkCnsm {

  private BnkPaymJsnSrv bnkPaymJsnSrv;

  public BeigeBnkCnsm(final BnkPaymJsnSrv pBnkPaymJsnSrv) {
    this.bnkPaymJsnSrv = pBnkPaymJsnSrv;
  }

  @Bean
  public Consumer<BnkPaymJsn> recievePayment() {
    return (bpayj) -> {
      try {
        this.bnkPaymJsnSrv.mkPayment(bpayj);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
  }
}
