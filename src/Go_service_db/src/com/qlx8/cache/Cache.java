package com.qlx8.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.qlx8.model.User;

public class Cache {
    private static Cache instance   =   null;
    private static Object lock      =   new Object();
    
    private ConcurrentHashMap<String, User> list = null;
    private ConcurrentHashMap<String, String> map = null;
    
    private Cache(){
        list = new ConcurrentHashMap<String, User>();
        map = new ConcurrentHashMap<String, String>();
    }
    
    public static Cache getInstance(){
        if(instance == null){
            synchronized (lock) {
                instance = new Cache();
            }
        }
        return instance;
    }
    
    
    public User get(String key){
        return list.get(key);
    }
    
    public void put(String key, User value){
        list.put(key, value);
        map.put(value.phone, key);
    }
    
    public User remove(String key){
        User u = list.remove(key);
        if(u != null){
            map.remove(u.getAttr(User.KEY_TOKEN));
        }
        return u;
    }
    
    public String check(String phone){
        return map.get(phone);
    }
    
}
