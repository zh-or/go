package com.go.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class T {
    
    public static final String EN_TYPE_MD5         =    "md5";
    public static final String EN_TYPE_SHA1        =    "sha1";

    
    public static void sleep(int t){
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public final static boolean CheckNull(String str){
        return str == null || "".equals(str);
    }
    
    public final static String createMsg(int type, String msg, String data){
        String res = "";
        res = String.format("{\"type\": %d, \"msg\": \"%s\", \"data\": %s}", type, msg, data);
        return res;
    }
    
    public final static String EnCode(String s, String entype) throws Exception{
        MessageDigest md = MessageDigest.getInstance(entype);
        md.update(s.getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer md5 = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            md5.append(Character.forDigit((digest[i] & 0xF0) >> 4, 16));
            md5.append(Character.forDigit((digest[i] & 0xF), 16));
        }
        s = md5.toString();
        return s;
    }
    
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    
    /**
     * 取中间文本
     * @param str 完整文本
     * @param l 左边文本
     * @param r 右边文本
     * @return
     */
    public static String getMid(String str, String l, String r){
        int _l = str.indexOf(l);
        if(_l == -1) return "";
        _l += l.length();
        int _r = str.indexOf(r, _l);
        if(_r == -1) return "";
        return str.substring(_l, _r);
    }
    
    
    public static int StrtoInt(String str){
        int res = 0;
        try {
            res = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            res = 0;
        }
        return res;
    }
    
    public static long StrtoLong(String str){
        long res = 0;
        try {
            res = Long.valueOf(str);
        } catch (NumberFormatException e) {
            res = 0l;
        }
        return res;
    }

    public static String formatException(Exception e){
        StackTraceElement ste = e.getStackTrace()[0];//只取第一个
        String exstr = String.format("filename:%s, classname:%s, methodname:%s, line:%d", ste.getFileName(), ste.getClassName(), ste.getMethodName(), ste.getLineNumber());
        return exstr;
    }
    
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String formatTime(long time){
        return sdf.format(new Date(time));
    }
}
