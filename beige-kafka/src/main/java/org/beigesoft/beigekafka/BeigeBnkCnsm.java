package org.beigesoft.beigekafka;

import org.beigesoft.busn.mdl.BnkPaymJsn;
import org.beigesoft.busn.BnkPaymJsnSrv;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class BeigeBnkCnsm {

  @Autowired
  private BnkPaymJsnSrv bnkPaymJsnSrv;

  @Bean
  public Consumer<BnkPaymJsn> recievePayment() {
    return (bpayj) -> {
      try {
        mkPayment(bpayj);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
  }


  public void mkPayment(BnkPaymJsn pBnkPayJsn) throws Exception {
/*
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
*/
    BigDecimal tott1 = new BigDecimal("100.77");
    BigDecimal tott2 = new BigDecimal("101.77");
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (pBnkPayJsn.getTotalAmount().equals(tott1)) {
      this.bnkPaymJsnSrv.mkTst1(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott2)) {
      this.bnkPaymJsnSrv.mkTst2(pBnkPayJsn);
    } else {
      this.bnkPaymJsnSrv.mkPaymentWrk(pBnkPayJsn);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for bnkPaymJsnSrv.</p>
   * @return BnkPaymJsnSrv
   **/
  public BnkPaymJsnSrv getBnkPaymJsnSrv() {
    return this.bnkPaymJsnSrv;
  }

  /**
   * <p>Setter for bnkPaymJsnSrv.</p>
   * @param pBnkPaymJsnSrv reference
   **/
  public void setBnkPaymJsnSrv(BnkPaymJsnSrv pBnkPaymJsnSrv) {
    this.bnkPaymJsnSrv = pBnkPaymJsnSrv;
  }
}
