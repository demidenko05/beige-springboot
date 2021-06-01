package org.beigesoft.busn.repo;

import org.beigesoft.busn.mdl.BnkPaym;
import org.beigesoft.busn.mdl.Invoice;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BnkPaymRep extends CrudRepository<BnkPaym, Long> {

  @Query("select sum(b.tot) from BnkPaym b where b.invc = ?1")
  BigDecimal selectSumTot(Invoice pInv);
}
