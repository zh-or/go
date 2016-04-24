package com.qlx8.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.qlx8.service.MainService;

public abstract class BaseDao {
    protected Connection conn;
    
    public BaseDao()  throws SQLException {
        conn = MainService.getInstanse().getDbConn();
    }
    
    public void close(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
