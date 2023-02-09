package com.example.abstractroutingdatasource.config;

import com.example.abstractroutingdatasource.ClientDataSourceRouter;
import com.example.abstractroutingdatasource.ClientDatasource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class RoutingTestConfiguration {

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

}
