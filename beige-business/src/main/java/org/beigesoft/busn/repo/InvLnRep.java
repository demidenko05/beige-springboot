package org.beigesoft.busn.repo;

import org.beigesoft.busn.mdl.InvLn;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InvLnRep extends CrudRepository<InvLn, Long> {

  @Query("select l from InvLn l join l.ownr o join o.custm c where l.id = ?1")
  InvLn findByIdCustm(Long pId);
}
