package org.beigesoft.busn;

import org.beigesoft.busn.mdl.Custm;
import org.beigesoft.busn.mdl.Itm;
import org.beigesoft.busn.mdl.InvLn;
import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.repo.InvLnRep;
import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.ItmRep;
import org.beigesoft.busn.repo.CustmRep;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

import static org.assertj.core.api.Assertions.assertThat;

@Service
public class TstEagerQuSrv {

  @Autowired
  private InvoiceRep invoiceRep;

  @Autowired
  private InvLnRep invLnRep;

  @Autowired
  private ItmRep itmRep;

  @Autowired
  private CustmRep custmRep;

  public Invoice createInv(long pCustId, String pCustNm, String[] pItmsNms, BigDecimal[] pPris) {
    Custm cust = new Custm();
    cust.setId(pCustId);
    cust.setNme(pCustNm);
    Invoice inv = new Invoice();
    inv.setCustm(cust);
    for (int i = 0; i < pItmsNms.length; i++) {
      InvLn il = new InvLn();
      il.setOwnr(inv);
      Itm itm  = new Itm();
      itm.setNme(pItmsNms[i]);
      il.setItm(itm);
      il.setPri(pPris[i]);
      il.setQuan(BigDecimal.ONE);
      il.setTot(il.getQuan().multiply(il.getPri()));
      inv.setTot(inv.getTot().add(il.getTot()));
      inv.getItsLns().add(il);
    }
    return inv;
  }

  public Invoice createInv2(Custm pCust, Itm[] pItms, BigDecimal[] pPris) {
    Invoice inv = new Invoice();
    inv.setCustm(pCust);
    for (int i = 0; i < pItms.length; i++) {
      InvLn il = new InvLn();
      il.setOwnr(inv);
      il.setItm(pItms[i]);
      il.setPri(pPris[i]);
      il.setQuan(BigDecimal.ONE);
      il.setTot(il.getQuan().multiply(il.getPri()));
      inv.setTot(inv.getTot().add(il.getTot()));
      inv.getItsLns().add(il);
    }
    return inv;
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Invoice saveInvoice(Invoice pInv) throws Exception {
    pInv.setCustm(this.custmRep.save(pInv.getCustm()));
    for (InvLn il : pInv.getItsLns()) {
      il.setItm(this.itmRep.save(il.getItm()));
    }
    return this.invoiceRep.save(pInv);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void deleteInvoice(Invoice pInv) throws Exception {
    this.invoiceRep.delete(pInv);
    this.custmRep.delete(pInv.getCustm());
    for (InvLn il : pInv.getItsLns()) {
      this.itmRep.delete(il.getItm());
    }
    /*for (InvLn il : pInv.getItsLns()) {
      this.invLnRep.delete(il);
    }*/
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public InvLn retInvLn(Long pId) throws Exception {
    Optional<InvLn> ilOp = this.invLnRep.findById(pId);
    if (!ilOp.isPresent()) {
      throw new Exception("There is no invoice line # " + pId);
    }
    return ilOp.get();
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public InvLn retInvLnCustm(Long pId) throws Exception {
    return this.invLnRep.findByIdCustm(pId);
  }

  //Simple getters and setters:

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

  /**
   * <p>Getter for invLnRep.</p>
   * @return InvLnRep
   **/
  public InvLnRep getInvLnRep() {
    return this.invLnRep;
  }

  /**
   * <p>Setter for invLnRep.</p>
   * @param pInvLnRep reference
   **/
  public void setInvLnRep(InvLnRep pInvLnRep) {
    this.invLnRep = pInvLnRep;
  }

  /**
   * <p>Getter for itmRep.</p>
   * @return ItmRep
   **/
  public ItmRep getItmRep() {
    return this.itmRep;
  }

  /**
   * <p>Setter for itmRep.</p>
   * @param pItmRep reference
   **/
  public void setItmRep(ItmRep pItmRep) {
    this.itmRep = pItmRep;
  }

  /**
   * <p>Getter for custmRep.</p>
   * @return CustmRep
   **/
  public CustmRep getCustmRep() {
    return this.custmRep;
  }

  /**
   * <p>Setter for custmRep.</p>
   * @param pCustmRep reference
   **/
  public void setCustmRep(CustmRep pCustmRep) {
    this.custmRep = pCustmRep;
  }
}
