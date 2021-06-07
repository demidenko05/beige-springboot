package org.beigesoft.busn.repo;

import org.beigesoft.busn.mdl.Invoice;

import java.math.BigDecimal;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Lock;

public interface InvoiceRep extends CrudRepository<Invoice, Long> {

  //for live tests purposes!
  Invoice findByTot(BigDecimal pTot);

  //for live tests purposes!
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from Invoice i where i.tot = ?1")
  Invoice findByTotLk(BigDecimal pTot);
}
