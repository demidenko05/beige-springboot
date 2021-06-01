This is Hibernate oriented!
E.g. it uses org.hibernate.annotations.Immutable

Make PostgreSQL database and user:
$ su - postgres
$ psql -f [path to beige-springboot/psql-cr-user-db.sql]

Build from root folder:
$ mvn clean install

Download kafka and unpack:
run kafka in different terminals:
$ bin/zookeeper-server-start.sh config/zookeeper.properties
$ bin/kafka-server-start.sh config/server.properties

Run beige-kafka:
$ java -jar beige-kafka/target/beige-kafka-0.1-SNAPSHOT.jar

To simulate simultaneous transactions run beige-bservice1 in another terminal:
$ java -jar beige-bservice1/target/beige-bservice1-0.1-SNAPSHOT.jar

Both services are triggered by kafka's topic bank-payment, simultaneity achieved also by Thread.sleep

List current topics:
$ bin/kafka-topics.sh --zookeeper localhost:2181 --list

This simulates that the bank approves a payment:
$ bin/kafka-console-producer.sh --topic bank-payment --bootstrap-server localhost:9092
>{"paymId":"229199920001","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"10818090","totalAmount":"11256.91"}

Preventing dirty writing transactions:
1. Using optimistic locking by long version (all entities in beige-business have it)
2. Using immutability as much as possible
3. Different kind of business transactions write only its own entities, e.g. reporting a message of a bank payment doesn't change the paid invoice.
4. The last resort is Isolation.SERIALIZABLE
5. Pessimistic locking should be avoided

Tests beige-business also populate database with sample invoices, the total of an invoice indicates which "live test" will be invoked:
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid TODO!!! can't get transactional annotated service work!!!??? It splits into two transactions - save bnk-pay and invoice
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
  of course, one of services will rollback because of org.hibernate.StaleObjectStateException

Eager optimal retrieving by queries.
Hibernate authors are little bit aggravated about eager default JPA standard (Hibernate_User_Guide.html#best-practices-fetching):
"...Prior to JPA, Hibernate used to have all associations as LAZY by default. However, when JPA 1.0 specification emerged, it was thought that not all providers would use Proxies. Hence, the @ManyToOne and the @OneToOne associations are now EAGER by default...."
See org.beigesoft.busn.repo.InvLnRep (test TstEagerQuSrv) - invoice line repository that retrieves line through invoice.customer including
That is dynamic retrieving for any tree and deep seems to be the best alternative (although Hibernate makes several queries for that, but BeigeORM makes this in a single query).

JPA/Hibernate tests are in beige-business/src/test...

Controlling with PostgreSQL logging:
$ nano /etc/postgresql/11/main/postgresql.conf
and make entry:
log_statement(all) 

run e.g. in H2 console:
BEGIN TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
SELECT * FROM bnk_paym;
COMMIT;

see logs:
$ cat /var/log/postgresql/postgresql-11-main.log

This is PostgreSQL oriented.
see /usr/share/doc/postgresql-doc-11/html/transaction-iso.html: ...i.e., PostgreSQL's Read Uncommitted mode behaves like Read Committed...


-------------------------------
Problem transactional annotated services (e.g. beige-kafka...BnkPaymJsnSrv) do not start transaction! Each CrudRepo.save() start and commit its own transaction!

But tests e.g. beige-business...TstEagerQuSrv.saveInvoice(...) work fine:
BEGIN
insert into custm (ver, nme, id) values ($1, $2, $3)
parameters: $1 = '0', $2 = 'OOO Orel', $3 = '28209288899'
insert into itm (ver, nme) values ($1, $2)
DETAIL:  parameters: $1 = '0', $2 = 'Product 1'
insert into itm (ver, nme) values ($1, $2)
parameters: $1 = '0', $2 = 'Service 1'
insert into invoice (ver, custm_id, descr, tot, tot_paid) values ($1, $2, $3, $4, $5)
...
COMMIT
They say: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.java-config
...The Spring Data JPA repositories support can be activated not only through an XML namespace but also by using an annotation through JavaConfig, as shown in the following example:

Example 55. Spring Data JPA repositories using JavaConfig
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
class ApplicationConfig {

  @Bean
  public DataSource dataSource() {

    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.HSQL).build();
  }
...
BUT CRUDRepo.save() already transactional annotated and auto-configured!
Services (that use several repo) must be autoconfigurable! Not only TESTS!!
