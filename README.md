# AbstractRoutingDatasource
https://www.baeldung.com/spring-abstract-routing-data-source 파트에 대한 실습

1. application.yml 파일 수정<br/>
  datasource:<br/>
    agens:
      driver-class-name: org.postgresql.Driver<br/>
      url: jdbc:postgresql://127.0.0.1:5432/database<br/>
      username: user<br/>
      password: password<br/>

  mysql:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/database
    username: user
    password: password
    
2. datasource 하위 레벨(ex> agens, mysql)을 수정했을 경우
  com.example.abstractroutingdatasource.config.RoutingConfiguration 
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
  해당부분의 수정도 필요하다.
  
  
