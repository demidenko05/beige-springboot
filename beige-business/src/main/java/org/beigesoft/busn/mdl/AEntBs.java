package org.beigesoft.busn.mdl;

import java.io.Serializable;

import javax.persistence.Transient;
import javax.persistence.PrePersist;
import javax.persistence.PostLoad;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.domain.Persistable;
import org.springframework.data.annotation.AccessType;

@MappedSuperclass
public abstract class AEntBs<ID> implements Serializable, Persistable<ID> {

  @Version
  private long ver; //even immutable entities should have version

  //Return the flag in the implementation of Persistable.isNew()
  //so that Spring Data repositories know whether
  //to call EntityManager.persist() or ….merge()
  //and generate insert or update
  @Transient
  @AccessType(AccessType.Type.PROPERTY)
  private boolean isNew = true;

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @PrePersist
  @PostLoad
  public void markNotNew() {
    this.isNew = false;
  }

  //to reinsert just deleted entity
  public void markNew() {
    this.isNew = true;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for ver.</p>
   * @return long
   **/
  public long getVer() {
    return this.ver;
  }

  /**
   * <p>Setter for ver.</p>
   * @param pVer reference
   **/
  public void setVer(final long pVer) {
    this.ver = pVer;
  }
}
