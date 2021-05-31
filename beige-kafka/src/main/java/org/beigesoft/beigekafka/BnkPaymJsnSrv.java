package org.beigesoft.beigekafka;

import org.beigesoft.busn.mdl.BnkPaymJsn;
import org.beigesoft.busn.mdl.BnkPaym;
import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BnkPaymJsnSrv {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  private BnkPaymRep bnkPaymRep;

  @Autowired
  private InvoiceRep invoiceRep;

  @Transactional
  public void mkPayment(BnkPaymJsn pBnkPayJsn) throws Exception {
    Optional<Invoice> invOp = this.invoiceRep.findById(pBnkPayJsn.getInvoiceId());
    if (!invOp.isPresent()) {
      //throw new Exception("There is no invoice # " + pBnkPayJsn.getInvoiceId());
      this.logger.error("There is no invoice #" + pBnkPayJsn.getInvoiceId());
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
