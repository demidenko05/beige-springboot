package org.beigesoft.busn.mdl;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
public abstract class AEntIdLngNme extends AEntIdLng {

  @Column
  @NotEmpty
  private String nme;

  //Simple getters and setters:
  /**
   * <p>Getter for nme.</p>
   * @return String
   **/
  public String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  public void setNme(final String pNme) {
    this.nme = pNme;
  }
}
