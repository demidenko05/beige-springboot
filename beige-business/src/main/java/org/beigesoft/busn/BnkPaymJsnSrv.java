package org.beigesoft.busn;

import org.beigesoft.busn.mdl.BnkPaymJsn;
import org.beigesoft.busn.mdl.BnkPaym;
import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.mdl.InvPaid;
import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;
import org.beigesoft.busn.repo.InvPaidRep;

//import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;


@Service
public class BnkPaymJsnSrv {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  private BnkPaymRep bnkPaymRep;

  @Autowired
  private InvoiceRep invoiceRep;

  @Autowired
  private InvPaidRep invPaidRep;

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED)
  public void mkTst1(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total "
        + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
    inv.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invoiceRep.save(inv);
    try {
      Thread.sleep(1000L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.SERIALIZABLE)
  public void mkTst2(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total "
        + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
    inv.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invoiceRep.save(inv);
    try {
      Thread.sleep(500L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED)
  public void mkTst3(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total "
        + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
    InvPaid inPd = inv.getInvPaid();
    if (inPd == null) {
      inPd = new InvPaid();
      inPd.setInv(inv);
    }
    inPd.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invPaidRep.save(inPd);
    logger.info("invPaid.totPaid=" + inPd.getTotPaid());
    try {
      Thread.sleep(800L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.SERIALIZABLE)
  public void mkTst4(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTot(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total "
        + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
    InvPaid inPd = inv.getInvPaid();
    if (inPd == null) {
      inPd = new InvPaid();
      inPd.setInv(inv);
    }
    inPd.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invPaidRep.save(inPd);
    logger.info("invPaid.totPaid=" + inPd.getTotPaid());
    try {
      Thread.sleep(900L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED)
  public void mkTst5(BnkPaymJsn pBnkPayJsn) throws Exception {
    Invoice inv = this.invoiceRep.findByTotLk(pBnkPayJsn.getTotalAmount());
    if (inv == null) {
      throw new Exception("Database is not populated for this test total "
        + pBnkPayJsn.getTotalAmount());
    }
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    bp.setInvc(inv);
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
    InvPaid inPd = inv.getInvPaid();
    if (inPd == null) {
      inPd = new InvPaid();
      inPd.setInv(inv);
    }
    inPd.setTotPaid(this.bnkPaymRep.selectSumTot(inv));
    this.invPaidRep.save(inPd);
    logger.info("invPaid.totPaid=" + inPd.getTotPaid());
    try {
      Thread.sleep(900L);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED)
  public void mkPaymentWrk(BnkPaymJsn pBnkPayJsn) throws Exception {
    BnkPaym bp = new BnkPaym();
    bp.setTot(pBnkPayJsn.getTotalAmount());
    bp.setPaymId(pBnkPayJsn.getPaymId());
    Optional<Invoice> invOp = this.invoiceRep
      .findById(pBnkPayJsn.getInvoiceId());
    if (!invOp.isPresent()) {
      this.logger.error("There is no invoice #" + pBnkPayJsn.getInvoiceId());
      bp.setDscrErr("!iid=" + pBnkPayJsn.getInvoiceId() + ", cid="
        + pBnkPayJsn.getCustmId() + ", cnm=" + pBnkPayJsn.getCustmNme());
    } else {
      bp.setInvc(invOp.get());
      if (!bp.getInvc().getId().equals(pBnkPayJsn.getCustmId())) {
        this.logger.error("Customer TIN doesn't match to invoice's one #"
          + pBnkPayJsn.getCustmId());
        bp.setDscrErr("!cid=" + pBnkPayJsn.getCustmId() + ", cnm="
          + pBnkPayJsn.getCustmNme());
      }
    }
    bp = this.bnkPaymRep.save(bp);
    this.logger.info("Saved bank payment #" + bp.getId() + ", payment#"
      + bp.getPaymId() + ", paid=" + bp.getTot() + ", invoice#"
        + pBnkPayJsn.getInvoiceId() + "/" + bp.getInvc());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for bnkPaymRep.</p>
   * @return BnkPaymRep
   **/
  public BnkPaymRep getBnkPaymRep() {
    return this.bnkPaymRep;
  }

  /**
   * <p>Setter for bnkPaymRep.</p>
   * @param pBnkPaymRep reference
   **/
  public void setBnkPaymRep(final BnkPaymRep pBnkPaymRep) {
    this.bnkPaymRep = pBnkPaymRep;
  }

  /**
   * <p>Getter for invoiceRep.</p>
   * @return InvoiceRep
   **/
  public InvoiceRep getInvoiceRep() {
    return this.invoiceRep;
  }

  /**
   * <p>Setter for invoiceRep.</p>
   * @param pInvoiceRep reference
   **/
  public void setInvoiceRep(final InvoiceRep pInvoiceRep) {
    this.invoiceRep = pInvoiceRep;
  }

  /**
   * <p>Getter for invPaidRep.</p>
   * @return InvPaidRep
   **/
  public InvPaidRep getInvPaidRep() {
    return this.invPaidRep;
  }

  /**
   * <p>Setter for invPaidRep.</p>
   * @param pInvPaidRep reference
   **/
  public void setInvPaidRep(final InvPaidRep pInvPaidRep) {
    this.invPaidRep = pInvPaidRep;
  }
}
