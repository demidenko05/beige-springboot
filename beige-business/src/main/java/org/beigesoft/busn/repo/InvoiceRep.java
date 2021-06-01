package org.beigesoft.busn.repo;

import org.beigesoft.busn.mdl.Invoice;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

public interface InvoiceRep extends CrudRepository<Invoice, Long> {

  //for live tests purposes!
  Invoice findByTot(BigDecimal pTot);
}
