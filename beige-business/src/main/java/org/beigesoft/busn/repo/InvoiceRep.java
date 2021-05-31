package org.beigesoft.busn.repo;

import org.beigesoft.busn.mdl.Invoice;

import org.springframework.data.repository.CrudRepository;

public interface InvoiceRep extends CrudRepository<Invoice, Long> {

}
