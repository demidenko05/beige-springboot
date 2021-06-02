package org.beigesoft.busn;

import org.beigesoft.busn.repo.InvoiceRep;
import org.beigesoft.busn.repo.BnkPaymRep;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories("org.beigesoft.busn.repo")
public class BsJpaConf {

  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Value("${dataSource.databaseName}")
  private String dbNm;

  @Value("${dataSourceClassName}")
  private String dsClNm;

  @Value("${dataSource.user}")
  private String usrNm;

  @Value("${dataSource.password}")
  private String usrPw;

  /*@Autowired Unsatisfied dependency expressed through field 'bnkPaymRep'
  private BnkPaymRep bnkPaymRep;

  @Autowired
  private InvoiceRep invoiceRep;*/

  @Bean
  public DataSource dataSource() {
    Properties props = new Properties();
    props.setProperty("dataSource.user", this.usrNm);
    props.setProperty("dataSource.password", this.usrPw);
    props.setProperty("dataSource.databaseName", this.dbNm);
    props.setProperty("dataSourceClassName", this.dsClNm);
    this.logger.info("Building HikariCP with " + props);
    HikariConfig hc = new HikariConfig(props);
    return new HikariDataSource(hc);
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory pEmf) {
    return new JpaTransactionManager(pEmf);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(true);
    LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
    lemfb.setDataSource(dataSource());
    lemfb.setJpaVendorAdapter(jpaVendorAdapter);
    lemfb.setPackagesToScan("org.beigesoft.busn.mdl");
    return lemfb;
  }

  /*@Bean
  public BnkPaymJsnSrv bnkPaymJsnSrv() {
    BnkPaymJsnSrv srv = new BnkPaymJsnSrv();
    srv.setBnkPaymRep(this.bnkPaymRep);
    srv.setInvoiceRep(this.invoiceRep);
    this.logger.info("Created BnkPaymJsnSrv " + srv);
    return srv;
  }*/

  /**
   * <p>Getter for dbNm.</p>
   * @return String
   **/
  public String getDbNm() {
    return this.dbNm;
  }

  /**
   * <p>Setter for dbNm.</p>
   * @param pDbNm reference
   **/
  public void setDbNm(String pDbNm) {
    this.dbNm = pDbNm;
  }

  /**
   * <p>Getter for dsClNm.</p>
   * @return String
   **/
  public String getDsClNm() {
    return this.dsClNm;
  }

  /**
   * <p>Setter for dsClNm.</p>
   * @param pDsClNm reference
   **/
  public void setDsClNm(String pDsClNm) {
    this.dsClNm = pDsClNm;
  }

  /**
   * <p>Getter for usrNm.</p>
   * @return String
   **/
  public String getUsrNm() {
    return this.usrNm;
  }

  /**
   * <p>Setter for usrNm.</p>
   * @param pUsrNm reference
   **/
  public void setUsrNm(String pUsrNm) {
    this.usrNm = pUsrNm;
  }

  /**
   * <p>Getter for usrPw.</p>
   * @return String
   **/
  public String getUsrPw() {
    return this.usrPw;
  }

  /**
   * <p>Setter for usrPw.</p>
   * @param pUsrPw reference
   **/
  public void setUsrPw(String pUsrPw) {
    this.usrPw = pUsrPw;
  }
}
