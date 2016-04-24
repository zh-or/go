package com.qlx8.model;

public class Friends {
    public int id;
    public int uid;
    public int fid;
    public String nickname;
    public long addtime;
    
    public Friends(int id, int uid, int fid, String nickname, long addtime) {
        this.id = id;
        this.uid = uid;
        this.fid = fid;
        this.nickname = nickname;
        this.addtime = addtime;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"uid\":");
        builder.append(uid);
        builder.append(",\"fid\":");
        builder.append(fid);
        builder.append(",\"nickname\":\"");
        builder.append(nickname);
        builder.append("\",\"addtime\":\"");
        builder.append(addtime);
        builder.append("\"}");
        return builder.toString();
    }
    
    
}
