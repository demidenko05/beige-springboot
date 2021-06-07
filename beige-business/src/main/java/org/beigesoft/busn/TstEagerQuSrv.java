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

  //populate DB wth sample data for live tests:
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void populDb() throws Exception {
/*
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
*/
    BigDecimal tot = new BigDecimal("100.77");
    Invoice inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv(28200000192299L, "OOO berezka",
      new String[] {"Product generic"}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }
    Custm custm = inv.getCustm();
    Itm itm = inv.getItsLns().get(0).getItm();
/*101.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use SERIALIZABLE level
to trigger this live test type in kafka-console-producer:
>{"paymId":"2","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"2","totalAmount":"101.77"}
*/
    tot = new BigDecimal("101.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }
/*102.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid
         - beige-bservice changes invoice.descr and invoice.totalPaid
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"2","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"2","totalAmount":"102.77"}
*/
    tot = new BigDecimal("102.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }
/*
103.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid
         - beige-bservice changes invoice.descr and invoice.totalPaid
         - they use SERIALIZABLE level
to trigger this live test type in kafka-console-producer:
>{"paymId":"3","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"3","totalAmount":"103.77"}
 */
    tot = new BigDecimal("103.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }

    tot = new BigDecimal("104.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }

    tot = new BigDecimal("105.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }

    tot = new BigDecimal("106.77");
    inv = findInvoice(tot);
    if (inv == null) {
      inv = createInv2(custm, new Itm[] {itm}, new BigDecimal[] {tot});
      inv = saveInvoice(inv);
    }
  }

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
  public Invoice findInvoice(BigDecimal pTot) throws Exception {
    return this.invoiceRep.findByTot(pTot);
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
