# AbstractRoutingDatasource
https://www.baeldung.com/spring-abstract-routing-data-source 파트에 대한 실습

1. application.yml 파일 수정<br/>
  datasource:<br/>
    agens:
      driver-class-name: org.postgresql.Driver<br/>
      url: jdbc:postgresql://127.0.0.1:5432/database<br/>
      username: user<br/>
      password: password<br/>

  mysql:<br/>
    driver-class-name: com.mysql.cj.jdbc.Driver<br/>
    url: jdbc:mysql://127.0.0.1:3306/database<br/>
    username: user<br/>
    password: password<br/>
    
2. datasource 하위 레벨(ex> agens, mysql)을 수정했을 경우<br/>
  com.example.abstractroutingdatasource.config.RoutingConfiguration <br/>
  @Bean<br/>
  @ConfigurationProperties("datasource.agens")<br/>
  public DataSourceProperties agensDatasourProperties(){<br/>
    return new DataSourceProperties();<br/>
  }<br/>

  @Bean<br/>
  @ConfigurationProperties("datasource.mysql")<br/>
  public DataSourceProperties mysqlDatasourProperties(){<br/>
    return new DataSourceProperties();<br/>
  }<br/>
  해당부분의 수정도 필요하다.<br/>
  
  
