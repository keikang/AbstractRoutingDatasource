package com.example.abstractroutingdatasource.config;

import com.example.abstractroutingdatasource.ClientDataSourceRouter;
import com.example.abstractroutingdatasource.ClientDatasource;
import com.example.abstractroutingdatasource.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "com.example.abstractroutingdatasource.repository")
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
/*    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getdomainsEntityManager(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("RoutingDataSource") DataSource dataSource) throws SQLException {
        System.out.println("RoutingTestConfiguration LocalContainerEntityManagerFactoryBean dataSource : "+dataSource.getConnection().toString());
        return builder.dataSource(dataSource)
                .packages("com.example.abstractroutingdatasource.repository")
                .build();
    }
*//*    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder
                                                        , @Qualifier("RoutingDataSource") DataSource dataSource) throws SQLException {
        return new JpaTransactionManager(getdomainsEntityManager(builder, dataSource).getObject());
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "com.example.abstractroutingdatasource.repository")
    static class JpaRepoConfig {

    }*/

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
    }

    @Bean(name = "transcationManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }*/
}
