package com.qlx8.util;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import util.util.Util;


public class T {

    public final static String getIpAddress(HttpServletRequest request) throws IOException {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }
    
    /**
     * 判断此字符串是否全由数字组成
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        if(Util.CheckNull(str)) return false;
        char[] cs = str.trim().toCharArray();
        for(int i = 0; i < cs.length; i++){
            if(cs[i] < 48 || cs[i] > 57) return false;
        }
        return true;
    }
    
    public static boolean obj_set(Object obj, String name, Object val){
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.set(obj, val);
            return true;
        } catch (Exception e) {}
        return false;
    }
    
    public static Object obj_get(Object obj, String name){
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {}
        return null;
    }
    
    
    public static void main(String[] args) {
        /*User u = new User(0, 0, "123", "456", "wtf", 123, 1, "def.png", 123456789, 0.0, 0.0, 123456789, 123456789, true);
        System.out.println(u);
        System.out.println("get:" + obj_get(u, "nickname"));
        System.out.println("set:" + obj_set(u, "nickname", "what the fuck"));
        System.out.println(u);*/
        System.out.println(Util.strHash("123"));
        System.out.println(Util.strHash("124"));
        isNumber("1234567890");
    }
}
