package com.qlx8.model;

public class Lx_Travels {
    public int id;
    public int uid;
    public int lvxing_id;
    public String txt;
    public long createtime;
    
    public Lx_Travels(int id, int uid, int lvxing_id, String txt, long createtime) {
        this.id = id;
        this.uid = uid;
        this.lvxing_id = lvxing_id;
        this.txt = txt;
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
        builder.append(",\"txt\":\"");
        builder.append(txt);
        builder.append("\",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }
    
    
}
