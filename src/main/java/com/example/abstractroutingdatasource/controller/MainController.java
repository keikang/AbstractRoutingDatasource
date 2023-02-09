package com.example.abstractroutingdatasource.controller;

import com.example.abstractroutingdatasource.ClientDatasource;
import com.example.abstractroutingdatasource.MonitoringService;
import com.example.abstractroutingdatasource.config.ClientDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    private final MonitoringService monitoringService;

    @GetMapping("/datasource/{dbName}")
    public ResponseEntity<?> getData(@PathVariable String dbName) throws SQLException {
        Object result = null;
        DataSource dataSource = null;
        if("agens".equals(dbName)){
            //System.out.println("MainController getData dbName : "+dbName);
            dataSource = ClientDatasource.getDatasource(ClientDatabase.AGENS);
            result = monitoringService.getData(dataSource);

        }else{
            dataSource = ClientDatasource.getDatasource(ClientDatabase.MYSQL);
            result = monitoringService.getData(dataSource);
        }

        return ResponseEntity.ok(result);
    }

}
