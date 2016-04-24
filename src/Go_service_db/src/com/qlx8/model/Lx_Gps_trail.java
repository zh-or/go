package com.qlx8.model;

public class Lx_Gps_trail {
    public int id;
    public int lvxing_id;
    public int uid;
    public double lng;
    public double lat;
    public double accuracy;
    public double altitude;
    public double bearing;
    public double speed;
    public long createtime;
    
    public Lx_Gps_trail(int id, int lvxing_id, int uid, double lng, double lat, double accuracy, double altitude, double bearing, double speed, long createtime) {
        this.id = id;
        this.lvxing_id = lvxing_id;
        this.uid = uid;
        this.lng = lng;
        this.lat = lat;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.bearing = bearing;
        this.speed = speed;
        this.createtime = createtime;
    }

    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"lvxing_id\":");
        builder.append(lvxing_id);
        builder.append(",\"uid\":");
        builder.append(uid);
        builder.append(",\"lng\":");
        builder.append(lng);
        builder.append(",\"lat\":");
        builder.append(lat);
        builder.append(",\"accuracy\":");
        builder.append(accuracy);
        builder.append(",\"altitude\":");
        builder.append(altitude);
        builder.append(",\"bearing\":");
        builder.append(bearing);
        builder.append(",\"speed\":");
        builder.append(speed);
        builder.append(",\"createtime\":");
        builder.append(createtime);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Lx_Gps_trail [id=" + id + ", lvxing_id=" + lvxing_id + ", uid=" + uid + ", lng=" + lng + ", lat=" + lat + ", accuracy=" + accuracy + ", altitude=" + altitude + ", bearing=" + bearing + ", speed=" + speed + ", createtime=" + createtime + "]";
    }
    
    
    
}
