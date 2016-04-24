package com.qlx8.model;

public class Lx_RoadBook {
    public int id;
    public int uid;
    public int lvxing_id;
    public String path;
    public long createtime;
    
    public Lx_RoadBook(int id, int uid, int lvxing_id, String path, long createtime) {
         this.id = id;
        this.uid = uid;
        this.lvxing_id = lvxing_id;
        this.path = path;
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"uid\":");
        builder.append(uid);
        builder.append(",\"lvxing_id\":");
        builder.append(lvxing_id);
        builder.append(",\"path\":\"");
        builder.append(path);
        builder.append("\",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }
    
    
}
