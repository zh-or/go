package com.qlx8.model;

public class Lx_Member {
    public int id;
    public int address;
    public String phone;
    public String nickname;
    public int sex;
    public String headpic;
    public long lastlogintime;
    public long regtime;
    public boolean isenable;
    public int lvxing_id;
    public long jointime;
    
    public Lx_Member(int id, int address, String phone, String nickname, int sex, String headpic, long lastlogintime, long regtime, boolean isenable, int lvxing_id, long jointime) {
        this.id = id;
        this.address = address; 
        this.phone = phone;
        this.nickname = nickname;
        this.sex = sex;
        this.headpic = headpic;
        this.lastlogintime = lastlogintime;
        this.regtime = regtime;
        this.isenable = isenable;
        this.lvxing_id = lvxing_id;
        this.jointime = jointime;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append("{\"address\":");
        builder.append(address);
        builder.append(",\"phone\":\"");
        builder.append(phone);
        builder.append("\",\"nickname\":\"");
        builder.append(nickname);
        builder.append("\",\"sex\":");
        builder.append(sex);
        builder.append(",\"headpic\":\"");
        builder.append(headpic);
        builder.append("\",\"lastlogintime\":");
        builder.append(lastlogintime);
        builder.append(",\"regtime\":");
        builder.append(regtime);
        builder.append(",\"isenable\":");
        builder.append(isenable);
        builder.append(",\"lvxing_id\":");
        builder.append(lvxing_id);
        builder.append(",\"jointime\":");
        builder.append(jointime);
        builder.append("}");
        return builder.toString();
    }
    
}
