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
if kafka blame - address already in use, then stop h2 console

Run beige-kafka:
$ java -jar beige-kafka/target/beige-kafka-0.1-SNAPSHOT.jar

To simulate simultaneous transactions run beige-bservice1 in another terminal:
$ java -jar beige-bservice1/target/beige-bservice1-0.1-SNAPSHOT.jar

Both services are triggered by kafka's topic bank-payment, simultaneity is achieved also by Thread.sleep

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
         - beige-bservice1 changes invoice.descr
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"1","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"1","totalAmount":"100.77"}
  of course, one of services will rollback because of org.hibernate.StaleObjectStateException

101.77 - beige-kafka (after saving bank payment) in the same transaction changes invoice.totalPaid
         - beige-bservice1 changes invoice.descr
         - they use SERIALIZABLE level
to trigger this live test type in kafka-console-producer:
>{"paymId":"2","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"2","totalAmount":"101.77"}
  org.postgresql.util.PSQLException: ERROR: could not serialize access due to concurrent update

b) good approach - two services writes their own entities (Invoice and InvPaid):
102.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid
         - beige-bservice1 changes invoice.descr and invoice.totalPaid
         - they use read-committed level
to trigger this live test type in kafka-console-producer:
>{"paymId":"2","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"2","totalAmount":"102.77"}
  there is no error. But invoice.totalPaid might have outdated data, e.g. after second time logs:
  beige-kafka - invPaid.totPaid=205.54
  beige-bservice1 - invoce.totPaid=102.77, invoce.invPaid.totPaid=102.77

103.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid
         - beige-bservice1 changes invoice.descr and invoice.totalPaid
         - they use SERIALIZABLE level
to trigger this live test type in kafka-console-producer:
>{"paymId":"3","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"3","totalAmount":"103.77"}
  org.postgresql.util.PSQLException: ERROR: could not serialize access due to read/write dependencies among transactions
  Detail: Reason code: Canceled on identification as a pivot, during write.

104.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid, read-committed level - never fail
       - beige-bservice1 just reads invoce.invPaid.totPaid, SERIALIZABLE level
         when error, then wait 1 sec and repeat up to 5 times

to trigger this live test type in kafka-console-producer:
>{"paymId":"4","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"4","totalAmount":"104.77"}
  same as read-committed for both, i.e. beige-bservice1 never fail and reports outdated data

105.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid
       - beige-bservice1 just reads invoce.invPaid.totPaid
         when error, then wait 1 sec and repeat up to 5 times
       - they use SERIALIZABLE level

to trigger this live test type in kafka-console-producer:
>{"paymId":"5","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"5","totalAmount":"105.77"}
  same as read-committed for both, i.e. beige-bservice1 never fail and reports outdated data
  BUT when beige-bservice1 also writes invoice (as in 103.77 test) then:
  beige-bservice1 will report exception, but try again until get up to date data, e.g. at first sending:
  beige-kafka: invPaid.totPaid=105.77
  beige-bservice1: invoce.invPaid.totPaid=105.77

106.77 - beige-kafka (after saving bank payment) in the same transaction changes InvPaid.totPaid AND LOCKS THE INVOICE with LockModeType.PESSIMISTIC_WRITE!
       - beige-bservice1 just reads invoce.invPaid.totPaid
       - they use read-committed level

to trigger this live test type in kafka-console-producer:
>{"paymId":"6","custmNme":"OOO berezka","custmId":"28200000192299","invoiceId":"6","totalAmount":"106.77"}
  this test always gives up-to date readed data, e.g on 4-th invocation:
  beige-kafka: invPaid.totPaid=427.08
  beige-bservice1: invoce.invPaid.totPaid=427.08
  but on 1-st invocation data CAN BE outdated (MS WIN, PG13, probably Thread.sleep() is the reason, i.e. non-simultaneously?):
  beige-kafka: invPaid.totPaid=106.77
  beige-bservice1: invoce.invPaid=null
  


Conclusion
Both read-committed and serializable allows outdated AT THE MOMENT reads.
For this particular business-process where bank-payment service must not rollback at all, read-uncommited seems to be more reliable,
i.e. it reads up-to date data (that must be commited ealier), and it's the cheapest isolation method, see file:///usr/share/doc/postgresql-doc-11/html/transaction-iso.html:
"...In contrast, a Read Committed or Repeatable Read transaction which wants to ensure data consistency may need to take out a lock on an entire table, which could block other users attempting to use that table, or it may use SELECT FOR UPDATE or SELECT FOR SHARE which not only can block other transactions but cause disk access."

To ensure that a report isn't outdated AT THE INVOCATION TIME in another transaction - only pessimistick locking work, serializable works only if reading transaction also writes.
But this is seems to be excessive, because a report can be outdated at any moment.
For this particular case bank-payment service should make/send the latest-updated report in another new transaction after registering the payment.

Optimistic locking, separating write access to entities and read-committed/uncommitted is reliable method.

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

MS Windows.
install git-bash, gnupg, JDK (e.g. 11 from https://jdk.java.net/java-se-ri/11)
it behaves as linux
copy/install into your Windows home folder files from/same as Linux one: .bashrc, .gitconfig, .keystore, .gnupg, kafka, maven, ant, h2...
use geany editor to keep UTF8 and LF line ending in source files
git-bash has almost all unix commands - ls, grep, find, gawk, df, tar...
It's not clear how to mount ext4 usb-stick yet, but "ls /dev" shows its partitions
after installing PostgreSQL change in "C:\Program Files\PostgreSQL\[VER]\data\postgresql.conf" entry:
client_encoding = UTF8		# actually, defaults to database
but PostgreSQL13.3 has UTF8 without it
psql might use 8-bit encording, so start power shell in "C:\Program Files\PostgreSQL\[VER]\scripts" then change code page:
> cmd.exe /c chcp 1251
then start runpsql.bat
> .\runpsql.bat
in psql make shure UTF8:
# SHOW SERVER_ENCODING;
create user and table:
# \i C:/Users/[your name]/git/beige-springboot/psql-cr-user-db.sql
list databases with encodings:
# \l
quit from psql
# \q
run git-bash, clone beige-springboot, change to it, run mvn clean install ...
run another git-bush in h2/bin, start "sh h2.sh" and .bashrc must have entry:
export H2DRIVERS=$HOME/.m2/repository/org/postgresql/postgresql/42.2.19/postgresql-42.2.19.jar
run in console to check UTF8:
insert into ITM (VER, NME)  values (0, 'Продукт АЩ ™');
select * from ITM; 

PROBLEMS:
* can't start kafka neither in git-bash (sh) nor in power-shell (bat)
 bat -  The input line is too long. The syntax of the command is incorrect.
 sh -  Error opening log file ...
 after moving kafka into C:\kafka power-shell works fine (git-bush gives that error):
C:\kafka> .\bin\windows\zookeeper-server-start.bat config\zookeeper.properties
C:\kafka> .\bin\windows\kafka-server-start.bat config\server.properties
C:\kafka> .\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list
C:\kafka> .\bin\windows\kafka-console-producer.bat --topic bank-payment --bootstrap-server localhost:9092

* Windows doesn't allow file names such as: nul.xml, aux.txt, ???
so git clone beige-blc will fail with error: invalid path 'src/main/resources/mysql/fldNmFs/nul.xml'
