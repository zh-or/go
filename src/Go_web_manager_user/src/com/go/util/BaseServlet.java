package com.go.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.json.JSONObject;

import java.lang.reflect.Method;

/**
 * @author OR
 */
public abstract class BaseServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        _service(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        _service(req, resp);
    }
    
    public void _service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String methodName = request.getParameter("m");
        
        try{
            if(methodName == null || methodName.trim() == ""){
                this._do(request, response);
                return;
            }
            if(!this._check(methodName, request, response)){
                return;
            }
        }catch(Exception e){
            this._exception(e, request, response);
            return;
        }
        Method method = null;
        try {
            method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (Exception e) {
            this._exception(new MethodNotFoundException("指定的方法未找到:" + methodName), request, response);
        }
        if(method != null){
            try {
                method.invoke(this, request, response);
            } catch (Exception e) {
                this._exception(e, request, response);
            }
        }
    }
    
    /**
     * 如果未指定参数m,默认调用此方法
     * @param request
     * @param response
     */
    public abstract void _do(HttpServletRequest request, HttpServletResponse response) throws Exception;
    
    /**
     * 执行方法前将首先调用此函数 用于各种检查
     * @param request
     * @param response
     * @return 返回 true 时则调用后面的函数, 否则则直接返回
     */
    public boolean _check(String method, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        return true;
    }
    
    /**
     * 如果反射发生了错误 则会调用子类实现的此方法
     * @param e
     * @param request
     * @param response
     */
    public abstract void _exception(Exception e, HttpServletRequest request, HttpServletResponse response);

    /*
     * 将请求转发至jsp页面
     * */
    public void _display(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getRequestDispatcher(path).forward(request, response);//转发
    }
    
    /**
     * 重定向
     * @throws IOException 
     */
    public void _redirect(String path, HttpServletRequest request, HttpServletResponse response) throws IOException{
    	response.sendRedirect(request.getContextPath() + path);//重定向
    }
    
    public void _send(JSONObject json, HttpServletResponse res) throws IOException{
        _send(json.toString(), res);
    }
    
    public void _send(String str, HttpServletResponse res) throws IOException{
    	PrintWriter print = res.getWriter();
    	print.write(str);
    	print.flush();
    }
}
