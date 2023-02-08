package com.example.abstractroutingdatasource;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MonitoringService {

    public Object getData(DataSource dataSource) throws SQLException {
        System.out.println("BoardService getData");
        Connection con = null;
        Statement st = null;
        List<HashMap<Object, String>> resultList = new ArrayList<>();
        ResultSet rs = null;

        try{
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT id, title, content FROM tb_board");

            while (rs.next()){
                HashMap<Object, String> obj = new HashMap<>();
                obj.put("id", rs.getString("id"));
                obj.put("title", rs.getString("title"));
                obj.put("content", rs.getString("content"));
                resultList.add(obj);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            rs.close();
            st.close();
            con.close();
        }
        return resultList;
    }

}
