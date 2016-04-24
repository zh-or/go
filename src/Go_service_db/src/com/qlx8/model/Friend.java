package com.qlx8.model;

public class Friend {
    public int id;
    public int address;
    public String phone;
    public long phonehash;
    public String password;
    public String nickname;
    public long nicknamehash;
    public int sex;
    public String headpic;
    public long birthday;
    public double reg_lat;
    public double reg_lng;
    public long lastlogintime;
    public long regtime;
    public boolean isenable;
    public String remark;
    public long addtime;
    
    public Friend(int id, int address, String phone, long phonehash, String password, String nickname, long nicknamehash, int sex, String headpic, long birthday, double reg_lat, double reg_lng, long lastlogintime, long regtime, boolean isenable, String remark, long addtime) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.phonehash = phonehash;
        this.password = password;
        this.nickname = nickname;
        this.nicknamehash = nicknamehash;
        this.sex = sex;
        this.headpic = headpic;
        this.birthday = birthday;
        this.reg_lat = reg_lat;
        this.reg_lng = reg_lng;
        this.lastlogintime = lastlogintime;
        this.regtime = regtime;
        this.isenable = isenable;
        this.remark = remark;
        this.addtime = addtime;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"address\":");
        builder.append(address);
        builder.append(",\"phone\":\"");
        builder.append(phone);
        builder.append("\",\"password\":\"");
        builder.append(password);
        builder.append("\",\"nickname\":\"");
        builder.append(nickname);
        builder.append("\",\"sex\":");
        builder.append(sex);
        builder.append(",\"headpic\":\"");
        builder.append(headpic);
        builder.append("\",\"birthday\":");
        builder.append(birthday);
        builder.append(",\"lastlogintime\":");
        builder.append(lastlogintime);
        builder.append(",\"regtime\":");
        builder.append(regtime);
        builder.append(",\"isenable\":");
        builder.append(isenable);
        builder.append(",\"remark\":\"");
        builder.append(remark);
        builder.append("\",\"addtime\":");
        builder.append(addtime);
        builder.append("}");
        return builder.toString();
    }
    
    
    
}
