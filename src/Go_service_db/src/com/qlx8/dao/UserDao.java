package com.qlx8.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import lotus.util.Util;

import com.qlx8.model.User;

public class UserDao extends BaseDao{

    public UserDao() throws SQLException {
        super();
    }

    public User query(String name, String password) {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select * from `user` where `user`.`phone` = ? and `user`.`password` = ?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User( rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getLong(7),
                        rs.getInt(8),
                        rs.getString(9),
                        rs.getLong(10),
                        rs.getDouble(11),
                        rs.getDouble(12),
                        rs.getLong(13),
                        rs.getLong(14),
                        rs.getBoolean(15));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return user;
    }

    public User query(int id) {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select * from `user` where `user`.`id` = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User( rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getLong(7),
                        rs.getInt(8),
                        rs.getString(9),
                        rs.getLong(10),
                        rs.getDouble(11),
                        rs.getDouble(12),
                        rs.getLong(13),
                        rs.getLong(14),
                        rs.getBoolean(15));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return user;
    }
    
    public int userTotal(){
        int total = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT count(`id`) FROM  `user`");
            rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return total;
    }
    
    public ArrayList<User> queryByPage(int page, int total) {
        ArrayList<User> users = new ArrayList<User>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        page--;
        if(page <= 0) page = 0;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `user` LIMIT ? , ?");
            stmt.setInt(1, page * total);
            stmt.setInt(2, total);
            rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User( rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getLong(4),
                                    rs.getString(5),
                                    rs.getString(6),
                                    rs.getLong(7),
                                    rs.getInt(8),
                                    rs.getString(9),
                                    rs.getLong(10),
                                    rs.getDouble(11),
                                    rs.getDouble(12),
                                    rs.getLong(13),
                                    rs.getLong(14),
                                    rs.getBoolean(15)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return users;
    }
    
    public boolean setEnable(int uid, boolean isenable){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {
            stmt = conn.prepareStatement("UPDATE  `go`.`user` SET  `isenable` =  ? WHERE  `user`.`id` = ?;");
            stmt.setBoolean(1, isenable);
            stmt.setInt(2, uid);
            isok = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return isok;
    }


    public boolean add(User user) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {
            stmt = conn.prepareStatement("INSERT INTO `go`.`user` (`id`, `address`, `phone`, `phonehash`, `password`, `nickname`, `sex`, `nicknamehash`, `headpic`, `birthday`, `reg_lat`, `reg_lng`, `lastlogintime`, `regtime`, `isenable`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, user.address);
            stmt.setString(2, user.phone);
            stmt.setLong(3, user.phonehash);
            stmt.setString(4, user.password);
            stmt.setString(5, user.nickname);
            stmt.setInt(6, user.sex);
            stmt.setLong(7, user.nicknamehash);
            stmt.setString(8, user.headpic);
            stmt.setLong(9, user.birthday);
            stmt.setDouble(10, user.reg_lat);
            stmt.setDouble(11, user.reg_lng);
            stmt.setLong(12, user.lastlogintime);
            stmt.setLong(13, user.regtime);
            stmt.setBoolean(14, user.isenable);
            isok = stmt.executeUpdate() > 0;
            if(isok){
                rs = stmt.getGeneratedKeys();
                if(rs != null && rs.next()){
                    user.id = rs.getInt(1);
                }else{/*发生了灵异事件*/
                    System.out.println("发生了灵异事件");
                    isok = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return isok;
    }

    public boolean updateInfo(int uid, String name, String value) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {
            stmt = conn.prepareStatement("UPDATE  `go`.`user` SET  `" + name + "` =  ? WHERE  `user`.`id` = ?;");
            stmt.setString(1, value);
            stmt.setInt(2, uid);
            isok = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        return isok;
    }
    
    public ArrayList<User> queryUserByPhoneHash(long hashstr){
        ArrayList<User> us = new ArrayList<User>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `user` where `user`.`phonehash` = ?");
            stmt.setLong(1, hashstr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                us.add(new User(    rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getLong(4),
                                    rs.getString(5),
                                    rs.getString(6),
                                    rs.getLong(7),
                                    rs.getInt(8),
                                    rs.getString(9),
                                    rs.getLong(10),
                                    rs.getDouble(11),
                                    rs.getDouble(12),
                                    rs.getLong(13),
                                    rs.getLong(14),
                                    rs.getBoolean(15)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        
        return us;
    }
    
    
    public ArrayList<User> queryUserByNickNameHash(long hashstr){
        ArrayList<User> us = new ArrayList<User>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `user` where `user`.`nicknamehash` = ?");
            stmt.setLong(1, hashstr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                us.add(new User(    rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getLong(4),
                                    rs.getString(5),
                                    rs.getString(6),
                                    rs.getLong(7),
                                    rs.getInt(8),
                                    rs.getString(9),
                                    rs.getLong(10),
                                    rs.getDouble(11),
                                    rs.getDouble(12),
                                    rs.getLong(13),
                                    rs.getLong(14),
                                    rs.getBoolean(15)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
            }
        }
        
        return us;
    }
    
    public User queryUserByPhone(String phone){
        ArrayList<User> us = queryUserByPhoneHash(Util.strHash(phone));
        for(User u : us){
            if(!Util.CheckNull(u.phone) && u.phone.equals(phone)) return u;
        }
        return null;
    }
    
    public User queryUserByNickname(String nickname){
        ArrayList<User> us = queryUserByNickNameHash(Util.strHash(nickname));
        for(User u : us){
            if(!Util.CheckNull(u.nickname) && u.nickname.equals(nickname)) return u;
        }
        return null;
    }

}
