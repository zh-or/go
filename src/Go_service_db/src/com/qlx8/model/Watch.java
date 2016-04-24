package com.qlx8.model;

public class Watch {
    public int id;
    public int uid;
    public int lvxing_id;
    public long createtime;
    
    public Watch(int id, int uid, int lvxing_id, long createtime) {
        this.id = id;
        this.uid = uid;
        this.lvxing_id = lvxing_id;
        this.createtime = createtime;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"uid\":");
        builder.append(uid);
        builder.append(",\"lvxing_id\":");
        builder.append(lvxing_id);
        builder.append(",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }



    @Override
    public String toString() {
        return String.format("Watch [id=%s, uid=%s, lvxing_id=%s, createtime=%s]", id, uid, lvxing_id, createtime);
    }
    
}
