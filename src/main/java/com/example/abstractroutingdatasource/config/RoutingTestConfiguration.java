package com.example.abstractroutingdatasource.config;

import com.example.abstractroutingdatasource.ClientDataSourceRouter;
import com.example.abstractroutingdatasource.ClientDatasource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@NoArgsConstructor
@EnableJpaRepositories(
        basePackages = ""
)
public class RoutingTestConfiguration {

/*    @Bean
    public ClientService clientService(){
        return new ClientService(clientDatasouSource());
    }*/
    @Bean
    public ClientDatasource clientDatasource(){
        System.out.println("RoutingTestConfiguration clientDatasource 생성자");
        return new ClientDatasource(getclientDatasSource());
    }
    @Bean("RoutingDataSource")
    public DataSource getclientDatasSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource agensDatasource = agensDatasource();
        DataSource mysqlDatasource = ageDatasource();

        targetDataSources.put(ClientDatabase.AGENS, agensDatasource);
        targetDataSources.put(ClientDatabase.MYSQL, mysqlDatasource);

        ClientDataSourceRouter clientDataSourceRouter = new ClientDataSourceRouter();
        clientDataSourceRouter.setTargetDataSources(targetDataSources);
        clientDataSourceRouter.setDefaultTargetDataSource(agensDatasource);
        return clientDataSourceRouter;
    }

    @Bean
    @ConfigurationProperties("datasource.agens")
    public DataSourceProperties agensDatasourProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("datasource.mysql")
    public DataSourceProperties mysqlDatasourProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource agensDatasource() {
        return agensDatasourProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public DataSource ageDatasource() {
        return mysqlDatasourProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean("routingLazyDataSource")
    public DataSource routingLazyDataSource(@Qualifier("RoutingDataSource") DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("routingLazyDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
/*    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(clientDatasouSource()).packages(Board.class).build();
    }*/

/*    @Bean(name = "transcationManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }*/
}