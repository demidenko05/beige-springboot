package org.beigesoft.busn.mdl;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;

//good approach for registering total payment for immutable (at current phase) invoice
@Entity
public class InvPaid extends AEntIdLnga {

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  private Invoice inv;

  @NotNull
  private BigDecimal totPaid = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for inv.</p>
   * @return Invoice
   **/
  public Invoice getInv() {
    return this.inv;
  }

  /**
   * <p>Setter for inv.</p>
   * @param pInv reference
   **/
  public void setInv(Invoice pInv) {
    this.inv = pInv;
  }

  /**
   * <p>Getter for totPaid.</p>
   * @return BigDecimal
   **/
  public BigDecimal getTotPaid() {
    return this.totPaid;
  }

  /**
   * <p>Setter for totPaid.</p>
   * @param pTotPaid reference
   **/
  public void setTotPaid(BigDecimal pTotPaid) {
    this.totPaid = pTotPaid;
  }
}
