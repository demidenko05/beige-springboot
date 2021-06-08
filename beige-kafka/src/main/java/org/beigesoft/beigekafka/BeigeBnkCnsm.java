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


  public void mkPayment(final BnkPaymJsn pBnkPayJsn) throws Exception {
    //see README.txt
    BigDecimal tott1 = new BigDecimal("100.77");
    BigDecimal tott2 = new BigDecimal("101.77");
    BigDecimal tott3 = new BigDecimal("102.77");
    BigDecimal tott4 = new BigDecimal("103.77");
    BigDecimal tott5 = new BigDecimal("104.77");
    BigDecimal tott6 = new BigDecimal("105.77");
    BigDecimal tott7 = new BigDecimal("106.77");
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (pBnkPayJsn.getTotalAmount().equals(tott1)) {
      this.bnkPaymJsnSrv.mkTst1(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott2)) {
      this.bnkPaymJsnSrv.mkTst2(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott3)) {
      this.bnkPaymJsnSrv.mkTst3(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott4)) {
      this.bnkPaymJsnSrv.mkTst4(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott5)) {
      this.bnkPaymJsnSrv.mkTst3(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott6)) {
      this.bnkPaymJsnSrv.mkTst4(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott7)) {
      this.bnkPaymJsnSrv.mkTst5(pBnkPayJsn);
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
  public void setBnkPaymJsnSrv(final BnkPaymJsnSrv pBnkPaymJsnSrv) {
    this.bnkPaymJsnSrv = pBnkPaymJsnSrv;
  }
}
