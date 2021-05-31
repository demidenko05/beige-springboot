package org.beigesoft.busn.mdl;

import java.math.BigDecimal;

import javax.persistence.Entity;
//import javax.validation.constraints.NotEmpty;
import javax.persistence.ManyToOne;

@Entity
public class InvLn extends AEntIdLnga {

  @ManyToOne
  private Invoice ownr;

  @ManyToOne
  private Itm itm;

  private BigDecimal pri = BigDecimal.ZERO;

  private BigDecimal quan = BigDecimal.ZERO;

  private BigDecimal tot = BigDecimal.ZERO;

  //Simple getters and setters:

  /**
   * <p>Getter for ownr.</p>
   * @return Invoice
   **/
  public Invoice getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  public void setOwnr(final Invoice pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  public Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  public void setItm(final Itm pItm) {
    this.itm = pItm;
  }

  /**
   * <p>Getter for pri.</p>
   * @return BigDecimal
   **/
  public BigDecimal getPri() {
    return this.pri;
  }

  /**
   * <p>Setter for pri.</p>
   * @param pPri reference
   **/
  public void setPri(final BigDecimal pPri) {
    this.pri = pPri;
  }

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  public BigDecimal getQuan() {
    return this.quan;
  }

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  public void setQuan(final BigDecimal pQuan) {
    this.quan = pQuan;
  }

  /**
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  public BigDecimal getTot() {
    return this.tot;
  }

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  public void setTot(final BigDecimal pTot) {
    this.tot = pTot;
  }
}
