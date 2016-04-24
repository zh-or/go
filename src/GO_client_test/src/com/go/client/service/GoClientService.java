package com.go.client.service;

import util.socket.client.Client;
import util.socket.common.ClientCallback;
import util.util.Util;

public class GoClientService extends ClientCallback{
	
	private Client 		socket			=	null;
	private String 		token			=	null;/*当前登陆账号token*/
	private long 		tokendeadline	=	0l;/*token 过期时间*/
	
	public GoClientService() {
		tokendeadline = System.currentTimeMillis();
	}
	
	/**
	 * 当前是否已登录
	 * @return
	 */
	public boolean IsLogined(){
		return tokendeadline < System.currentTimeMillis() || Util.CheckNull(token);
	}
	
	public void login(){
		
	}
	
	@Override
	public void onClose(Client sc) {
		
	}
	
	@Override
	public void onMessageRecv(Client sc, byte[] msg) {
		
	}
	
}
