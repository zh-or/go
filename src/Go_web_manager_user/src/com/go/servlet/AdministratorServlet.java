package com.go.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.format.Format;
import util.log.Log;
import util.util.Util;

import com.go.service.MainService;
import com.go.util.BaseServlet;


@WebServlet(urlPatterns="/administrator", name="AdministratorServlet")
public class AdministratorServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MainService main;
	
    public AdministratorServlet() {
    	main = MainService.getInstanse();
    }

	@Override
	public void _do(HttpServletRequest request, HttpServletResponse response) throws Exception {
		login(request, response);
	}

	@Override
	public void _exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		main.getLog().log(Log.L_E, "administrator servlet error, info:", Format.formatException(e));
	}

	@Override
	public boolean _check(String method, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!"login".equals(method)){
			Object isadmin = request.getSession().getAttribute("isadmin");
			if(isadmin == null){
				_redirect("/administrator", request, response);
				return false;
			}
		}
		return true;
	}
	
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Object isadmin = request.getSession().getAttribute("isadmin");
		if(isadmin != null){
			_redirect("/administrator/main.do", request, response);
		}else{
			String password = request.getParameter("pwd");
			if(!Util.CheckNull(password)){
				if(password.equals(main.getConfig().getStringValue("ADMIN", "PASSWORD", "admin"))){
					request.getSession().setAttribute("isadmin", "logined");
					_redirect("/administrator/main.do", request, response);
					return;
				}
			}
			_display("/WEB-INF/admin/admin_login.jsp", request, response);
		}
	}
	
	public void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		_display("/WEB-INF/admin/admin_main.jsp", request, response);
	}
	
	public void getinfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		_send("{}", response);
	}
	
}
