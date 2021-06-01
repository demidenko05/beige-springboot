package org.beigesoft.bservice1;

import org.beigesoft.busn.mdl.BnkPaymJsn;
import org.beigesoft.busn.mdl.BnkPaym;
import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

@Service
public class BService1Srv {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  private BnkPaymRep bnkPaymRep;

  @Autowired
  private InvoiceRep invoiceRep;

  public void mkPayment(BnkPaymJsn pBnkPayJsn) throws Exception {
/*
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
*/
    this.logger.info("Get bank payment #" + pBnkPayJsn.getPaymId()
      + ", total=" + pBnkPayJsn.getTotalAmount() + ", invoice#" + pBnkPayJsn.getInvoiceId());
    BigDecimal tott1 = new BigDecimal("100.77");
    if (pBnkPayJsn.getTotalAmount().equals(tott1)) {
      try {
        Thread.currentThread().sleep(1000L);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      mkTst1(pBnkPayJsn);
    } else {
      this.logger.info("Non-test bank payment!!!");
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void mkTst1(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    inv.setDescr("BService1 " + new Date());
    this.invoiceRep.save(inv);
    try {
      Thread.currentThread().sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for bnkPaymRep.</p>
   * @return BnkPaymRep
   **/
  public final BnkPaymRep getBnkPaymRep() {
    return this.bnkPaymRep;
  }

  /**
   * <p>Setter for bnkPaymRep.</p>
   * @param pBnkPaymRep reference
   **/
  public final void setBnkPaymRep(final BnkPaymRep pBnkPaymRep) {
    this.bnkPaymRep = pBnkPaymRep;
  }

  /**
   * <p>Getter for invoiceRep.</p>
   * @return InvoiceRep
   **/
  public final InvoiceRep getInvoiceRep() {
    return this.invoiceRep;
  }

  /**
   * <p>Setter for invoiceRep.</p>
   * @param pInvoiceRep reference
   **/
  public final void setInvoiceRep(final InvoiceRep pInvoiceRep) {
    this.invoiceRep = pInvoiceRep;
  }
}
