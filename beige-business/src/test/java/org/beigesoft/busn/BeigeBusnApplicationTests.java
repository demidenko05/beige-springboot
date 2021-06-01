package org.beigesoft.busn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.busn.mdl.Invoice;
import org.beigesoft.busn.mdl.InvLn;
import org.beigesoft.busn.mdl.Itm;
import org.beigesoft.busn.mdl.Custm;
import org.beigesoft.busn.repo.CustmRep;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional; //single transaction for method plus automatically rollback when test

@SpringBootTest
class BeigeBusnApplicationTests {

  @Autowired
  private TstCustmSrv tstCustmSrv;

  @Autowired
  private TstEagerQuSrv tstEagerQuSrv;

  @Test
  //@Transactional
  //Invoking two transnational methods, marking new
  void test1() throws Exception {
    Custm[] cusAr = this.tstCustmSrv.createCustms();
    for (Custm cust : cusAr) {
      assertThat(cust.isNew()).isTrue();
    }
    this.tstCustmSrv.saveCustms(cusAr);
//PostgreSQL log:
/*
SHOW TRANSACTION ISOLATION LEVEL
SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN
insert into custm (nme, id) values ($1, $2)
parameters: $1 = 'OOO Berezka', $2 = '28200000012977'
insert into custm (nme, id) values ($1, $2)
parameters: $1 = 'OOO Topol', $2 = '282000000171761'
COMMIT
SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED
*/
    for (Custm cust : cusAr) {
      assertThat(cust.isNew()).isFalse();
    }
    this.tstCustmSrv.removeCustms(cusAr);
//PostgreSQL log:
/*
SHOW TRANSACTION ISOLATION LEVEL
SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN
select custm0_.id as id1_1_0_, custm0_.nme as nme2_1_0_ from custm custm0_ where custm0_.id=$1
parameters: $1 = '28200000012977'
select custm0_.id as id1_1_0_, custm0_.nme as nme2_1_0_ from custm custm0_ where custm0_.id=$1
parameters: $1 = '282000000171761'
delete from custm where id=$1
parameters: $1 = '28200000012977'
delete from custm where id=$1
parameters: $1 = '282000000171761'
COMMIT
SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED
 */
  }

  @Test
  //@Transactional
  //Optimistic locking
  void test2() throws Exception {
    Custm[] cusAr = this.tstCustmSrv.createCustms();
    for (Custm cust : cusAr) {
      assertThat(cust.isNew()).isTrue();
    }
    this.tstCustmSrv.saveCustms(cusAr);
    cusAr[1].setNme(cusAr[1].getNme() + " ch1");
    long vw = cusAr[1].getVer();
    Custm detch = cusAr[1];
    cusAr[1] = this.tstCustmSrv.saveCustm(cusAr[1]);
    assertThat(cusAr[1].getVer()).isEqualTo(vw + 1L);
    cusAr[1].setNme(cusAr[1].getNme() + " ch2");
    cusAr[1] = this.tstCustmSrv.saveCustm(cusAr[1]);
    assertThat(cusAr[1].getVer()).isEqualTo(vw + 2L);
    boolean wasExeption = false;
    try {
      detch.setNme(cusAr[1].getNme() + " ch3");
      this.tstCustmSrv.saveCustm(detch);
    } catch (Exception ex) { //org.hibernate.StaleObjectStateException
      wasExeption = true;
      ex.printStackTrace();
    }
    assertThat(wasExeption).isTrue();
    this.tstCustmSrv.removeCustms(cusAr);
  }


  @Test
  //Eager retrieving by query - fetch invoice line through invoice.customer including
  void test3() throws Exception {
    Invoice inv = this.tstEagerQuSrv.createInv(28209288899L, "OOO Orel",
      new String[] {"Product 1", "Service 1"},
        new BigDecimal[] {new BigDecimal("122.34"), new BigDecimal("422.30")});
    assertThat(inv.getItsLns()).hasSizeGreaterThan(0);
    inv = this.tstEagerQuSrv.saveInvoice(inv);
    assertThat(inv.getItsLns()).hasSizeGreaterThan(0);
    InvLn il = this.tstEagerQuSrv.retInvLnCustm(inv.getItsLns().get(0).getId());
/*
BEGIN
select invln0_.id as id1_2_, invln0_.ver as ver2_2_, invln0_.itm_id as itm_id6_2_, invln0_.ownr_id as ownr_id7_2_, invln0_.pri as pri3_2_, invln0_.quan as quan4_2_, invln0_.tot as tot5_2_ from inv_ln invln0_ inner join invoice invoice1_ on invln0_.ownr_id=invoice1_.id inner join custm custm2_ on invoice1_.custm_id=custm2_.id where invln0_.id=$1
parameters: $1 = '5'
select itm0_.id as id1_4_0_, itm0_.ver as ver2_4_0_, itm0_.nme as nme3_4_0_ from itm itm0_ where itm0_.id=$1
parameters: $1 = '3'
select invoice0_.id as id1_3_0_, invoice0_.ver as ver2_3_0_, invoice0_.custm_id as custm_id6_3_0_, invoice0_.descr as descr3_3_0_, invoice0_.tot as tot4_3_0_, invoice0_.tot_paid as tot_paid5_3_0_, custm1_.id as id1_1_1_, custm1_.ver as ver2_1_1_, custm1_.nme as nme3_1_1_ from invoice invoice0_ left outer join custm custm1_ on invoice0_.custm_id=custm1_.id where invoice0_.id=$1
parameters: $1 = '6'
COMMIT
 */
    assertThat(il.getOwnr().getCustm().getNme()).isNotNull();
    this.tstEagerQuSrv.deleteInvoice(inv);
  }

  @Test
  //populate DB wth sample data for live tests:
  void populDb() throws Exception {
/*
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
*/
    BigDecimal tot = new BigDecimal("100.77");
    Invoice inv = this.tstEagerQuSrv.findInvoice(tot);
    if (inv == null) {
      inv = this.tstEagerQuSrv.createInv(28200000192299L, "OOO berezka",
      new String[] {"Product generic"}, new BigDecimal[] {tot});
      inv = this.tstEagerQuSrv.saveInvoice(inv);
    }
  }
}
