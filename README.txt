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

Run in beige-business to populate database with sample invoices (it's also invoked on mvn clean install):
$ mvn spring-boot:run -Dspring-boot.run.arguments="--populDb=true"
the total of a sample invoice indicates which "live test" will be invoked:

a) wrong approach - two services writes the same entity (invoice.totalPaid and invoice.descr):
100.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
  of course, one of services will rollback because of org.hibernate.StaleObjectStateException

101.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice changes invoice.descr
         - they use SERIALIZABLE level
to trigger this live test type in kafka-console-producer:
>{"paymId":"2","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"2","totalAmount":"101.77"}
  org.postgresql.util.PSQLException: ERROR: could not serialize access due to concurrent update

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
