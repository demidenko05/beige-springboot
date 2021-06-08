package org.beigesoft.busn;

import org.beigesoft.busn.mdl.Custm;
import org.beigesoft.busn.repo.CustmRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

@Service
public class TstCustmSrv {

  @Autowired
  private CustmRep custmRep;

  public Custm[] createCustms() throws Exception {
    Custm custm1 = new Custm();
    custm1.setId(28200000012977L);
    custm1.setNme("OOO Berezka");

    Custm custm2 = new Custm();
    custm2.setId(282000000171761L);
    custm2.setNme("OOO Topol");
    return new Custm[] {custm1, custm2};
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_UNCOMMITTED)
  public void saveCustms(final Custm[] pCustArr) throws Exception {
    for (int i = 0; i < pCustArr.length; i++) {
      pCustArr[i] = this.custmRep.save(pCustArr[i]);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_UNCOMMITTED)
  public Custm saveCustm(final Custm pCust) throws Exception {
    return this.custmRep.save(pCust);
  }

  @Transactional(propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_UNCOMMITTED)
  public void removeCustms(final Custm[] pCustArr) throws Exception {
    for (Custm cust : pCustArr) {
      this.custmRep.delete(cust);
    }
 }

  //Simple getters and setters:

  /**
   * <p>Getter for custmRep.</p>
   * @return CustmRep
   **/
  public CustmRep getCustmRep() {
    return this.custmRep;
  }

  /**
   * <p>Setter for custmRep.</p>
   * @param pCustmRep reference
   **/
  public void setCustmRep(final CustmRep pCustmRep) {
    this.custmRep = pCustmRep;
  }
}
