package org.beigesoft.beigekafka;

import org.beigesoft.busn.mdl.BnkPaymJsn;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

/*import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;*/

@Configuration
//@EnableTransactionManagement
public class BeigeBnkCnsm {

  private BnkPaymJsnSrv bnkPaymJsnSrv;

  public BeigeBnkCnsm(final BnkPaymJsnSrv pBnkPaymJsnSrv) {
    this.bnkPaymJsnSrv = pBnkPaymJsnSrv;
  }

  @Bean
  //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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
