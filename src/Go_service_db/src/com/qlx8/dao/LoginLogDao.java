package com.qlx8.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import lotus.util.Util;

import com.qlx8.model.LoginLog;
import com.qlx8.service.MainService;

public class LoginLogDao {
    private static LoginLogDao  lldao       =   null;
    private static Object       lock        =   new Object();
    
    private static final int   capacity     =   1000;
    
    private BlockingQueue<LoginLog> logs;
    private MainService             service;
    
    private LoginLogDao(){
        logs = new ArrayBlockingQueue<LoginLog>(capacity);
        service = MainService.getInstanse();
        new Thread(new SaveThread()).start();
    }
    
    
    public static LoginLogDao getInstance(){
        if(lldao == null){
            synchronized (lock) {
                if(lldao == null){
                    lldao = new LoginLogDao();
                }
            }
        }
        return lldao;
    }
    
    public void pushLog(LoginLog log){
        logs.add(log);
    }
    
    private class SaveThread implements Runnable{

        @Override
        public void run() {
            
            ArrayList<LoginLog> loginlogs = new ArrayList<LoginLog>();
            LoginLog l = null;
            while(true){
                try {
                    while(true){
                        l = logs.poll(2, TimeUnit.MINUTES);
                        loginlogs.add(l);
                        if(l == null || loginlogs.size() >= 100){/*100条了再怎么也要写出去*/
                            break;
                        }
                    }
                } catch (InterruptedException e) {}
                
                if(loginlogs.size() > 0){
                    Connection conn = null;
                    
                    try {
                        boolean autocommit = false;
                        conn = service.getDbConn();
                        autocommit = conn.getAutoCommit();
                        Statement stmt = conn.createStatement();
                        conn.setAutoCommit(false);
                        for(LoginLog log : loginlogs){
                            if(log != null) stmt.addBatch(Log2Sql(log));
                        }
                        stmt.executeBatch();
                        conn.commit();
                        conn.setAutoCommit(autocommit);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        if(conn != null){
                            try {
                                conn.close();
                            } catch (SQLException e) {}
                        }
                    }
                    if(l == null && logs.size() > 0){/*>0且有空表示退出了*/
                        System.out.println("loginlog exit!");
                        break;
                    }
                    loginlogs.clear();
                }
                Util.SLEEP(500);
            }
        }
    }
    
    private String Log2Sql(LoginLog log){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `go`.`loginlog` (`id`, `uid`, `lastlogintime`, `lng`, `lat`, `addr`, `ip`, `ip_int`, `device_type`) VALUES (NULL, ");
        sb.append(log.uid);
        sb.append(", ");
        sb.append(log.time);
        sb.append(", ");
        sb.append(log.lng);
        sb.append(", ");
        sb.append(log.lat);
        sb.append(", '");
        sb.append(log.addr);
        sb.append("', '");
        sb.append(log.ip);
        sb.append("', ");
        sb.append(log.ip_int);
        sb.append(", '");
        sb.append(log.device_type);
        sb.append("');");
        return sb.toString();
    }
    
    
    public static void main(String[] args) {
        LoginLog log = new LoginLog(0, 1, 123, 0.1, 0.2, "adr", "127.0.0.1", 123, "web");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `go`.`loginlog` (`id`, `uid`, `lastlogintime`, `lng`, `lat`, `addr`, `ip`, `ip_int`, `device_type`) VALUES (NULL, ");
        sb.append(log.uid);
        sb.append(", ");
        sb.append(log.time);
        sb.append(", ");
        sb.append(log.lng);
        sb.append(", ");
        sb.append(log.lat);
        sb.append(", '");
        sb.append(log.addr);
        sb.append("', '");
        sb.append(log.ip);
        sb.append("', ");
        sb.append(log.ip_int);
        sb.append(", '");
        sb.append(log.device_type);
        sb.append("');");
        System.out.println(sb.toString());
    }
}
