package org.beigesoft.busn.mdl;

import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//import org.springframework.lang.Nullable;

@MappedSuperclass
public abstract class AEntIdLng extends AEntBs<Long> {

  @Id
  private Long id; //Natural ID, e.g. TIN, Phone number, etc

  /**
   * <p>Getter for id.</p>
   * @return Long
   **/
  @Override
  //@Nullable never!
  public Long getId() {
    return this.id;
  }

  //Hibernate_User_Guide.html#naturalid
  @Override
  public boolean equals(final Object pOther) {
    if (this == pOther) {
      return true;
    }
    if (pOther == null || this.getClass() != pOther.getClass()) {
      return false;
    }
    AEntIdLng other = (AEntIdLng) pOther;
    return Objects.equals(this.id, other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  //Simple getters and setters:
  /**
   * <p>Setter for id.</p>
   * @param pId reference
   **/
  public void setId(final Long pId) {
    this.id = pId;
  }
}
