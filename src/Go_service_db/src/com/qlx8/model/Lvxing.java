package com.qlx8.model;

public class Lvxing {
    public int id;
    public int type;
    public int owner;
    public int number;
    public String name;
    public String explain;
    public double begin_lng;
    public double begin_lat;
    public double end_lng;
    public double end_lat;
    public int begin_addr;
    public int end_addr;
    public long begintime;
    public long endtime;
    public long createtime;
    public boolean delete;
    
    public Lvxing(int id, int type, int owner, int number, String name, String explain, double begin_lng, double begin_lat, double end_lng, double end_lat, int begin_addr, int end_addr, long begintime, long endtime, long createtime, boolean delete) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.number = number;
        this.name = name;
        this.explain = explain;
        this.begin_lng = begin_lng;
        this.begin_lat = begin_lat;
        this.end_lng = end_lng;
        this.end_lat = end_lat;
        this.begin_addr = begin_addr;
        this.end_addr = end_addr;
        this.begintime = begintime;
        this.endtime = endtime;
        this.createtime = createtime;
        this.delete = delete;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"type\":");
        builder.append(type);
        builder.append(",\"owner\":");
        builder.append(owner);
        builder.append(",\"number\":");
        builder.append(number);
        builder.append(",\"name\":\"");
        builder.append(name);
        builder.append(",\"explain\":\"");
        builder.append(explain);
        builder.append("\",\"begin_lng\":");
        builder.append(begin_lng);
        builder.append(",\"begin_lat\":");
        builder.append(begin_lat);
        builder.append(",\"end_lng\":");
        builder.append(end_lng);
        builder.append(",\"end_lat\":");
        builder.append(end_lat);
        builder.append(",\"begin_addr\":");
        builder.append(begin_addr);
        builder.append(",\"end_addr\":");
        builder.append(end_addr);
        builder.append(",\"begintime\":");
        builder.append(begintime);
        builder.append(",\"endtime\":");
        builder.append(endtime);
        builder.append(",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }
    
    
    
}
