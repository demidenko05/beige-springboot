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

List current topics:
$ bin/kafka-topics.sh --zookeeper localhost:2181 --list

This simulates that the bank approves a payment:
$ bin/kafka-console-producer.sh --topic bank-payment --bootstrap-server localhost:9092
>{"paymId":"229199920001","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"10818090","totalAmount":"11256.91"}

Eager optimal retrieving by queries.
Hibernate authors are little bit aggravated about eager default JPA standard (Hibernate_User_Guide.html#best-practices-fetching):
"...Prior to JPA, Hibernate used to have all associations as LAZY by default. However, when JPA 1.0 specification emerged, it was thought that not all providers would use Proxies. Hence, the @ManyToOne and the @OneToOne associations are now EAGER by default...."
See org.beigesoft.busn.repo.InvLnRep (test TstEagerQuSrv) - invoice line repository that retrieves line through invoice.customer including
That is dynamic retrieving for any tree and deep seems to be the best alternative (although Hibernate makes several queries for that, but BeigeORM makes this in a single query).

Preventing dirty writing transactions:
1. Using optimistic locking by long version
2. Using immutability as many as possible
3. Different kind of business transactions write only its own entities, e.g. reporting a message of a bank payment doesn't change the paid invoice.
4. The last resort is Isolation.SERIALIZABLE
5. Pessimistic locking should be avoided

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
