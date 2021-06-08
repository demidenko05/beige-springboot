package org.beigesoft.busn.mdl;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class BnkPaym extends AEntIdLnga {

  @NotNull
  private Long paymId; //from JSON

  @ManyToOne
  //Null if not found by ID
  private Invoice invc;

  @NotNull
  private BigDecimal tot;

  //if invoice or customer is wrong then report:
  private String dscrErr;

  //Simple getters and setters:
  /**
   * <p>Getter for paymId.</p>
   * @return Long
   **/
  public Long getPaymId() {
    return this.paymId;
  }

  /**
   * <p>Setter for paymId.</p>
   * @param pPaymId reference
   **/
  public void setPaymId(final Long pPaymId) {
    this.paymId = pPaymId;
  }

  /**
   * <p>Getter for invc.</p>
   * @return Invoice
   **/
  public Invoice getInvc() {
    return this.invc;
  }

  /**
   * <p>Setter for invc.</p>
   * @param pInvc reference
   **/
  public void setInvc(final Invoice pInvc) {
    this.invc = pInvc;
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

  /**
   * <p>Getter for dscrErr.</p>
   * @return String
   **/
  public String getDscrErr() {
    return this.dscrErr;
  }

  /**
   * <p>Setter for dscrErr.</p>
   * @param pDscrErr reference
   **/
  public void setDscrErr(final String pDscrErr) {
    this.dscrErr = pDscrErr;
  }
}
