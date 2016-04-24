package com.qlx8.model;

public class Lx_Travels_Attachment {
    public int id;
    public int tid;
    public int type;
    public String path;
    public long createtime;
    
    public Lx_Travels_Attachment(int id, int tid, int type, String path, long createtime) {
        this.id = id;
        this.tid = tid;
        this.type = type;
        this.path = path;
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"tid\":");
        builder.append(tid);
        builder.append(",\"type\":");
        builder.append(type);
        builder.append(",\"path\":\"");
        builder.append(path);
        builder.append("\",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }
    
    
}
