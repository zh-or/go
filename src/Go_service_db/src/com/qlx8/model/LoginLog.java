package com.qlx8.model;


public class LoginLog {
    public int id;
    public int uid;
    public long time;
    public double lng;
    public double lat;
    public String addr;
    public String ip;
    public int ip_int;
    public String device_type;
    
    public LoginLog(int id, int uid, long time, double lat, double lng, String addr, String ip, int ip_int, String device_type) {
       
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
        this.addr = addr;
        this.ip = ip;
        this.ip_int = ip_int;
        this.device_type = device_type;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"uid\":\"");
        builder.append(uid);
        builder.append("\",\"time\":");
        builder.append(time);
        builder.append(",\"lat\":");
        builder.append(lat);
        builder.append(",\"lon\":");
        builder.append(lng);
        builder.append(",\"addr\":\"");
        builder.append(addr);
        builder.append(",\"ip\":\"");
        builder.append(ip);
        builder.append("\",\"ip_int\":");
        builder.append(ip_int);
        builder.append(",\"device_type\":\"");
        builder.append(device_type);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoginLog [id=");
        builder.append(id);
        builder.append(", uid=");
        builder.append(uid);
        builder.append(", time=");
        builder.append(time);
        builder.append(", lng=");
        builder.append(lng);
        builder.append(", lat=");
        builder.append(lat);
        builder.append(", addr=");
        builder.append(addr);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", ip_int=");
        builder.append(ip_int);
        builder.append(", device_type=");
        builder.append(device_type);
        builder.append("]");
        return builder.toString();
    }
    
    
}
