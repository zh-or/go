package com.qlx8.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.qlx8.model.Friend;

public class FriendsDao extends BaseDao{

    public FriendsDao() throws SQLException {
        super();
    }

    
    public ArrayList<Friend> queryAllFriends(int uid){
        ArrayList<Friend> fs = new ArrayList<Friend>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select a.*, b.remark, b.addtime from `user` a left join friends b on a.id = b.fid where b.uid = ?");
            stmt.setInt(1, uid);
            rs = stmt.executeQuery();
            while(rs.next()){
                fs.add(new Friend(  rs.getInt(1),
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
                                    rs.getBoolean(15),
                                    rs.getString(16),
                                    rs.getLong(17)));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {}
        }
        return fs;
    }
    
    public boolean add(int uid, int fid, String remark){
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("INSERT INTO `friends` (`id`, `uid`, `fid`, `remark`, `addtime`) VALUES (NULL, ?, ?, ?, ?);");
            stmt.setInt(1, uid);
            stmt.setInt(2, fid);
            stmt.setString(3, remark);
            stmt.setLong(4, System.currentTimeMillis());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {}
        }
        
        return false;
    }
    
    
    public boolean updateRemark(int id, String remark){
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("UPDATE  `friends` SET  `remark` =  ? WHERE  `friends`.`id` = ?;");
            stmt.setString(1, remark);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {}
        }
        return false;
    }
    
    public boolean deleteFriend(int id){
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM `friends` WHERE  `friends`.`id` = ?;");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {}
        }
        return false;
    }
}
