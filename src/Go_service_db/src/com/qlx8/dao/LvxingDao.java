package com.qlx8.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.qlx8.model.Lvxing;
import com.qlx8.model.Lx_Member;

public class LvxingDao extends BaseDao {

    public LvxingDao() throws SQLException {
        super();
    }

    
    public boolean add(Lvxing lx){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {
            stmt = conn.prepareStatement("INSERT INTO `go`.`lvxing` (`id`, `type`, `owner`, `number`, `name`, `explain`, `begin_lng`, `begin_lat`, `end_lng`, `end_lat`, `begin_addr`, `end_addr`, `begintime`, `endtime`, `createtime`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, lx.type);
            stmt.setInt(2, lx.owner);
            stmt.setInt(3, lx.number);
            stmt.setString(4, lx.name);
            stmt.setString(5, lx.explain);
            stmt.setDouble(6, lx.begin_lng);
            stmt.setDouble(7, lx.begin_lat);
            stmt.setDouble(8, lx.end_lng);
            stmt.setDouble(9, lx.end_lat);
            stmt.setInt(10, lx.begin_addr);
            stmt.setInt(11, lx.end_addr);
            stmt.setLong(12, lx.begintime);
            stmt.setLong(13, lx.endtime);
            stmt.setLong(14, lx.createtime);
            isok = stmt.executeUpdate() > 0;
            if(isok){
                rs = stmt.getGeneratedKeys();
                if(rs != null && rs.next()){
                    lx.id = rs.getInt(1);
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
    
    public boolean updateinfo(int id, String name, String value){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {
            stmt = conn.prepareStatement("UPDATE `lvxing` SET  `?` =  ? WHERE  `lvxing`.`id` = ?;");
            stmt.setString(1, name);
            stmt.setString(2, value);
            stmt.setInt(3, id);
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
    
    
    public boolean delete(int id){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isok = false;
        try {/*由于此表关联过多, 故 不删除实体数据, 标记flag则以.*/
            stmt = conn.prepareStatement("UPDATE `lvxing` SET  `delete` =  1 WHERE  `lvxing`.`id` = ?;");
            stmt.setInt(1, id);
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
    
    /**
     * 
     * @param uid
     * @param p 页数, 从1开始
     * @param ptotal 每页显示总条数
     * @return
     */
    public ArrayList<Lvxing> queryByPage(int uid, int p, int ptotal){
        ArrayList<Lvxing> lxs = new ArrayList<Lvxing>();
        p = p -1;
        if(p <= 0) p = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `lvxing` WHERE  `owner` = ? and `lvxing`.`delete` = 0 LIMIT ? , ?");
            stmt.setInt(1, uid);
            stmt.setInt(2, p * ptotal);
            stmt.setInt(3, ptotal);
            rs = stmt.executeQuery();
            while(rs.next()){
                lxs.add(new Lvxing(rs.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getDouble(7),
                                rs.getDouble(8),
                                rs.getDouble(9),
                                rs.getDouble(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getLong(13),
                                rs.getLong(14),
                                rs.getLong(15),
                                rs.getBoolean(16)));
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
        return lxs;
    }
    
    public Lvxing queryByLvxingId(int lxid){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `lvxing` WHERE  `lvxing`.`id` = ? and `lvxing`.`delete` = 0;");
            stmt.setInt(1, lxid);
            rs = stmt.executeQuery();
            while(rs.next()){
                return new Lvxing(rs.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getDouble(7),
                                rs.getDouble(8),
                                rs.getDouble(9),
                                rs.getDouble(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getLong(13),
                                rs.getLong(14),
                                rs.getLong(15),
                                rs.getBoolean(16));
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
        return null;
    }
    
    
    public Lvxing queryByNumber(int number){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `lvxing` WHERE  `lvxing`.`number` = ? and `lvxing`.`delete` = 0;");
            stmt.setInt(1, number);
            rs = stmt.executeQuery();
            while(rs.next()){
                return new Lvxing(rs.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getDouble(7),
                                rs.getDouble(8),
                                rs.getDouble(9),
                                rs.getDouble(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getLong(13),
                                rs.getLong(14),
                                rs.getLong(15),
                                rs.getBoolean(16));
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
        return null;
    }
    
    
    
    /**
     * 
     * @param uid
     * @param p 页数, 从1开始
     * @param ptotal 每页显示总条数
     * @return
     */
    public ArrayList<Lvxing> queryByEndAddr(int end_addr, int p, int ptotal){
        ArrayList<Lvxing> lxs = new ArrayList<Lvxing>();
        p = p -1;
        if(p <= 0) p = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `lvxing` WHERE  `end_addr` = ? and `lvxing`.`delete` = 0 LIMIT ? , ?");
            stmt.setInt(1, end_addr);
            stmt.setInt(2, p * ptotal);
            stmt.setInt(3, ptotal);
            rs = stmt.executeQuery();
            while(rs.next()){
                lxs.add(new Lvxing(rs.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getDouble(7),
                                rs.getDouble(8),
                                rs.getDouble(9),
                                rs.getDouble(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getLong(13),
                                rs.getLong(14),
                                rs.getLong(15),
                                rs.getBoolean(16)));
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
        return lxs;
    }
    
    
    /**
     * 
     * @param uid
     * @param p 页数, 从1开始
     * @param ptotal 每页显示总条数
     * @return
     */
    public ArrayList<Lvxing> queryByBeginAddr(int begin_addr, int p, int ptotal){
        ArrayList<Lvxing> lxs = new ArrayList<Lvxing>();
        p = p -1;
        if(p <= 0) p = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM  `lvxing` WHERE  `begin_addr` = ? and `lvxing`.`delete` = 0 LIMIT ? , ?");
            stmt.setInt(1, begin_addr);
            stmt.setInt(2, p * ptotal);
            stmt.setInt(3, ptotal);
            rs = stmt.executeQuery();
            while(rs.next()){
                lxs.add(new Lvxing(rs.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getDouble(7),
                                rs.getDouble(8),
                                rs.getDouble(9),
                                rs.getDouble(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getLong(13),
                                rs.getLong(14),
                                rs.getLong(15),
                                rs.getBoolean(16)));
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
        return lxs;
    }
    
    public ArrayList<Lx_Member> queryLxMember(int lvxing_id, int p, int pagetotal){
        ArrayList<Lx_Member> ms = new ArrayList<Lx_Member>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        p = p -1;
        if(p <= 0) p = 0;
        try {
            stmt = conn.prepareStatement("select a.id, a.address, a.phone, a.nickname, a.sex, a.headpic, a.lastlogintime, a.regtime, a.isenable, b.lvxing_id, b.jointime from `user` a left join `lx_member` b on a.id = b.uid where b.lvxing_id = ? LIMIT ? , ?;");
            stmt.setInt(1, lvxing_id);
            stmt.setInt(2, p * pagetotal);
            stmt.setInt(3, pagetotal);
            rs = stmt.executeQuery();
            while(rs.next()){
                ms.add(new Lx_Member(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getLong(7),
                        rs.getLong(8),
                        rs.getBoolean(9),
                        rs.getInt(10),
                        rs.getLong(11)));
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
        
        return ms;
    }
}
