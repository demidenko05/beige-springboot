package org.beigesoft.busn.mdl;

import java.math.BigDecimal;

public class BnkPaymJsn {

  private Long paymId;

  private Long invoiceId;

  private Long custmId;

  private String custmNme;

  private BigDecimal totalAmount;

  //Simple getters and setters:
  /**
   * <p>Getter for paymId.</p>
   * @return Long
   **/
  public final Long getPaymId() {
    return this.paymId;
  }

  /**
   * <p>Setter for paymId.</p>
   * @param pPaymId reference
   **/
  public final void setPaymId(final Long pPaymId) {
    this.paymId = pPaymId;
  }

  /**
   * <p>Getter for invoiceId.</p>
   * @return Long
   **/
  public final Long getInvoiceId() {
    return this.invoiceId;
  }

  /**
   * <p>Setter for invoiceId.</p>
   * @param pInvoiceId reference
   **/
  public final void setInvoiceId(final Long pInvoiceId) {
    this.invoiceId = pInvoiceId;
  }

  /**
   * <p>Getter for custmId.</p>
   * @return Long
   **/
  public final Long getCustmId() {
    return this.custmId;
  }

  /**
   * <p>Setter for custmId.</p>
   * @param pCustmId reference
   **/
  public final void setCustmId(final Long pCustmId) {
    this.custmId = pCustmId;
  }

  /**
   * <p>Getter for custmNme.</p>
   * @return String
   **/
  public final String getCustmNme() {
    return this.custmNme;
  }

  /**
   * <p>Setter for custmNme.</p>
   * @param pCustmNme reference
   **/
  public final void setCustmNme(final String pCustmNme) {
    this.custmNme = pCustmNme;
  }

  /**
   * <p>Getter for totalAmount.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotalAmount() {
    return this.totalAmount;
  }

  /**
   * <p>Setter for totalAmount.</p>
   * @param pTotalAmount reference
   **/
  public final void setTotalAmount(final BigDecimal pTotalAmount) {
    this.totalAmount = pTotalAmount;
  }
}
