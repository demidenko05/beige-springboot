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

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void mkTst1(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    inv.setDescr("BService1 " + new Date());
    this.invoiceRep.save(inv);
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public void mkTst2(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    inv.setDescr("BService1 " + new Date());
    this.invoiceRep.save(inv);
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void mkTst3(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    inv.setDescr("BService1 " + new Date());
    inv.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invoiceRep.save(inv);
    logger.info("invoce.totPaid=" + inv.getTotPaid());
    if (inv.getInvPaid() != null) {
      logger.info("invoce.invPaid.totPaid=" + inv.getInvPaid().getTotPaid());
    }
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public void mkTst4(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total " + pBnkPayJsn.getTotalAmount());
    }
    inv.setDescr("BService1 " + new Date());
    inv.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invoiceRep.save(inv);
    logger.info("invoce.totPaid=" + inv.getTotPaid());
    if (inv.getInvPaid() != null) {
      logger.info("invoce.invPaid.totPaid=" + inv.getInvPaid().getTotPaid());
    }
    try {
      Thread.sleep(1000L);
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
