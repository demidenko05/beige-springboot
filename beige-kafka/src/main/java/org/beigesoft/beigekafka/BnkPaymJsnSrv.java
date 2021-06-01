package org.beigesoft.beigekafka;

import org.beigesoft.busn.mdl.BnkPaymJsn;
import org.beigesoft.busn.mdl.BnkPaym;
import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableTransactionManagement
@Service
//@Repository
public class BnkPaymJsnSrv {

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
    BigDecimal tott1 = new BigDecimal("100.77");
    if (pBnkPayJsn.getTotalAmount().equals(tott1)) {
      try {
        Thread.currentThread().sleep(1000L);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      mkTst1(pBnkPayJsn);
    } else {
      mkPaymentWrk(pBnkPayJsn);
    }
  }

  //do not start transaction!!!!!!
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void mkTst1(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp); //TODO???? it start and commits here????
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#" + bp.getPaymId()
      + ", paid=" + bp.getTot() + ", invoice#" + pBnkPayJsn.getInvoiceId()
        + "/" + bp.getInvc());
    inv.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invoiceRep.save(inv);
    try {
      Thread.currentThread().sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional
  public void mkPaymentWrk(BnkPaymJsn pBnkPayJsn) throws Exception {
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    Optional<Invoice> invOp = this.invoiceRep.findById(pBnkPayJsn.getInvoiceId());
    if (!invOp.isPresent()) {
      this.logger.error("There is no invoice #" + pBnkPayJsn.getInvoiceId());
      bp.setDscrErr("!iid=" + pBnkPayJsn.getInvoiceId() + ", cid=" + pBnkPayJsn.getCustmId()
        + ", cnm=" + pBnkPayJsn.getCustmNme());
    } else {
      bp.setInvc(invOp.get());
      if (!bp.getInvc().getId().equals(pBnkPayJsn.getCustmId())) {
        this.logger.error("Customer TIN doesn't match to invoice's one #" + pBnkPayJsn.getCustmId());
        bp.setDscrErr("!cid=" + pBnkPayJsn.getCustmId() + ", cnm=" + pBnkPayJsn.getCustmNme());
      }
    }
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#" + bp.getPaymId()
      + ", paid=" + bp.getTot() + ", invoice#" + pBnkPayJsn.getInvoiceId()
        + "/" + bp.getInvc());
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
