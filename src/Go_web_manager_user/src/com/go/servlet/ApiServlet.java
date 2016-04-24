package com.go.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.go.util.BaseServlet;

/**
 * 页面处理servlet
 * @author yangfan
 */
@WebServlet(name="ApiServlet", urlPatterns="/api_admin")
public class ApiServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void _do(HttpServletRequest request, HttpServletResponse response) throws Exception {
		_send("do some thing...", response);
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		_display("main.jsp", request, response);
	}

	
	@Override
	public void _exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
	    try {
	        
	        
            _send("", response);
        } catch (IOException e1) {}
	}
}
