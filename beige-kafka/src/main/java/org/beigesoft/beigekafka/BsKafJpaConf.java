package org.beigesoft.beigekafka;

import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;
import org.beigesoft.busn.BnkPaymJsnSrv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableTransactionManagement
public class BsKafJpaConf {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  private BnkPaymRep bnkPaymRep;

  @Autowired
  private InvoiceRep invoiceRep;

  @Bean
  public BnkPaymJsnSrv bnkPaymJsnSrv() {
    BnkPaymJsnSrv srv = new BnkPaymJsnSrv();
    srv.setBnkPaymRep(this.bnkPaymRep);
    srv.setInvoiceRep(this.invoiceRep);
    this.logger.info("Created BnkPaymJsnSrv " + srv);
    return srv;
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
  public void setBnkPaymRep(BnkPaymRep pBnkPaymRep) {
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
  public void setInvoiceRep(InvoiceRep pInvoiceRep) {
    this.invoiceRep = pInvoiceRep;
  }
}
