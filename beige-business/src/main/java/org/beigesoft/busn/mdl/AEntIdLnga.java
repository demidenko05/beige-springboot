package org.beigesoft.busn.mdl;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.lang.Nullable;

@MappedSuperclass
public abstract class AEntIdLnga extends AEntBs<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * <p>Getter for id.</p>
   * @return Long
   **/
  @Override
  @Nullable
  public Long getId() {
    return this.id;
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
