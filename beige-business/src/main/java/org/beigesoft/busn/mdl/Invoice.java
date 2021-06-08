package org.beigesoft.busn.mdl;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;

@Entity
public class Invoice extends AEntIdLnga {

  @ManyToOne
  @NotNull
  private Custm custm;

  @NotNull
  private BigDecimal tot = BigDecimal.ZERO;

  //bad approach (embedded changeable field)
  private BigDecimal totPaid = BigDecimal.ZERO;

  private String descr;

  @OneToMany(mappedBy = "ownr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<InvLn> itsLns = new ArrayList<>();

  //non-owned entity (invoice services must not change it)
  @OneToOne(mappedBy = "inv", cascade = {CascadeType.MERGE},
    fetch = FetchType.LAZY)
  private InvPaid invPaid;

  //Simple getters and setters:
  /**
   * <p>Getter for custm.</p>
   * @return Custm
   **/
  public Custm getCustm() {
    return this.custm;
  }

  /**
   * <p>Setter for custm.</p>
   * @param pCustm reference
   **/
  public void setCustm(final Custm pCustm) {
    this.custm = pCustm;
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
  public void setTotPaid(final BigDecimal pTotPaid) {
    this.totPaid = pTotPaid;
  }

  /**
   * <p>Getter for descr.</p>
   * @return String
   **/
  public String getDescr() {
    return this.descr;
  }

  /**
   * <p>Setter for descr.</p>
   * @param pDescr reference
   **/
  public void setDescr(final String pDescr) {
    this.descr = pDescr;
  }

  /**
   * <p>Getter for itsLns.</p>
   * @return List<InvLn>
   **/
  public List<InvLn> getItsLns() {
    return this.itsLns;
  }

  /**
   * <p>Setter for itsLns.</p>
   * @param pItsLns reference
   **/
  public void setItsLns(final List<InvLn> pItsLns) {
    this.itsLns = pItsLns;
  }

  /**
   * <p>Getter for invPaid.</p>
   * @return InvPaid
   **/
  public InvPaid getInvPaid() {
    return this.invPaid;
  }

  /**
   * <p>Setter for invPaid.</p>
   * @param pInvPaid reference
   **/
  public void setInvPaid(final InvPaid pInvPaid) {
    this.invPaid = pInvPaid;
  }
}
