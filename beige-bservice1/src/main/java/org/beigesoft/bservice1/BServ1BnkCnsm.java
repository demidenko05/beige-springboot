package org.beigesoft.bservice1;

import org.beigesoft.busn.mdl.BnkPaymJsn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BServ1BnkCnsm {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  private BService1Srv bService1Srv;

  public BServ1BnkCnsm(final BService1Srv pBnkPaymJsnSrv) {
    this.bService1Srv = pBnkPaymJsnSrv;
  }

  @Bean
  public Consumer<BnkPaymJsn> handlePayment() {
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
... see README.txt
*/
    this.logger.info("Get bank payment #" + pBnkPayJsn.getPaymId()
      + ", total=" + pBnkPayJsn.getTotalAmount() + ", invoice#" + pBnkPayJsn.getInvoiceId());
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
      this.bService1Srv.mkTst1(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott2)) {
      this.bService1Srv.mkTst2(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott3)) {
      this.bService1Srv.mkTst3(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott4)) {
      this.bService1Srv.mkTst4(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott7)) {
      this.bService1Srv.mkTst6(pBnkPayJsn);
    } else if (pBnkPayJsn.getTotalAmount().equals(tott5)
      || pBnkPayJsn.getTotalAmount().equals(tott6)) {
      int cntAtm = 0;
      boolean isDn = false;
      while (!isDn && cntAtm++ < 6) {
        try {
          this.bService1Srv.mkTst5(pBnkPayJsn);
          isDn = true;
        } catch (Exception ex) {
          ex.printStackTrace();
          Thread.sleep(1000L);
        }
      }
    } else {
      this.logger.info("Non-test bank payment!!!");
    }
  }
}
