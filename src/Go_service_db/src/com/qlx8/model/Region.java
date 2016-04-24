package com.qlx8.model;

public class Region {
    public int id;
    public String son_ids;
    public int fid;
    public int id_lv1;
    public int id_lv2;
    public int id_lv3;
    public String name;
    public String fullname;
    public String pinyin;
    public String pinyincsv;
    public String py;
    public int level;
    public String code;
    public String code_lv1;
    public String code_lv2;
    public String code_lv3;
    public String area_code;
    public String zip_code;
    public double lng;
    public double lat;
    
    public Region(int id, String son_ids, int fid, int id_lv1, int id_lv2, int id_lv3, String name, String fullname, String pinyin, String pinyincsv, String py, int level, String code, String code_lv1, String code_lv2, String code_lv3, String area_code, String zip_code, double lng, double lat) {
        this.id = id;
        this.son_ids = son_ids;
        this.fid = fid;
        this.id_lv1 = id_lv1;
        this.id_lv2 = id_lv2;
        this.id_lv3 = id_lv3;
        this.name = name;
        this.fullname = fullname;
        this.pinyin = pinyin;
        this.pinyincsv = pinyincsv;
        this.py = py;
        this.level = level;
        this.code = code;
        this.code_lv1 = code_lv1;
        this.code_lv2 = code_lv2;
        this.code_lv3 = code_lv3;
        this.area_code = area_code;
        this.zip_code = zip_code;
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"son_ids\":\"");
        builder.append(son_ids);
        builder.append("\",\"fid\":");
        builder.append(fid);
        builder.append(",\"id_lv1\":");
        builder.append(id_lv1);
        builder.append(",\"id_lv2\":");
        builder.append(id_lv2);
        builder.append(",\"id_lv3\":");
        builder.append(id_lv3);
        builder.append(",\"name\":\"");
        builder.append(name);
        builder.append("\",\"fullname\":\"");
        builder.append(fullname);
        builder.append("\",\"pinyin\":\"");
        builder.append(pinyin);
        builder.append("\",\"pinyincsv\":\"");
        builder.append(pinyincsv);
        builder.append("\",\"py\":\"");
        builder.append(py);
        builder.append("\",\"level\":");
        builder.append(level);
        builder.append(",\"code\":\"");
        builder.append(code);
        builder.append("\",\"code_lv1\":\"");
        builder.append(code_lv1);
        builder.append("\",\"code_lv2\":\"");
        builder.append(code_lv2);
        builder.append("\",\"code_lv3\":\"");
        builder.append(code_lv3);
        builder.append("\",\"area_code\":\"");
        builder.append(area_code);
        builder.append("\",\"zip_code\":\"");
        builder.append(zip_code);
        builder.append("\",\"lng\":");
        builder.append(lng);
        builder.append(",\"lat\":");
        builder.append(lat);
        builder.append("}");
        return builder.toString();
    }
    
    
}
