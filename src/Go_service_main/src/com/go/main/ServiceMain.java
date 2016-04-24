package com.go.main;

import com.go.base.Context;

/**
 * 消息推送&聊天信息转发?
 */
public class ServiceMain {
    static Context ctx;
    
    public static void main(String[] args) {    	
    	String path = "./config.ini";
	    if(args != null && args.length > 0){
	    	path = args[0];
	    }
        ctx = Context.getInstance();
        ctx.init(path);
        /*standby*/
    }
}
