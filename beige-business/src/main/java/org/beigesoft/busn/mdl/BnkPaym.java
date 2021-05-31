package org.beigesoft.busn.mdl;

import java.math.BigDecimal;

import org.hibernate.annotations.Immutable;
import javax.persistence.Entity;
//import javax.validation.constraints.NotEmpty;
import javax.persistence.ManyToOne;

@Entity
@Immutable
public class BnkPaym extends AEntIdLng {

  @ManyToOne
  private Invoice invc;

  private BigDecimal tot;

  //Simple getters and setters:
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
}
