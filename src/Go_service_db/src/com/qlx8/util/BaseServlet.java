package com.qlx8.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.json.JSONObject;
import util.util.Util;

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
     * 检查参数是否为空
     * @param pars 参数key数组
     * @param request
     * @return 如果有为空的则返回 false
     */
    public boolean _checkparameter(String[] pars, HttpServletRequest request){
    	if(pars == null || pars.length <= 0) return true;
    	for(int i = 0; i < pars.length; i++){
    		if(Util.CheckNull(request.getParameter(pars[i]))){
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean _filterStrings(String[] pars, String val){
        if(pars == null || pars.length <= 0) return false;
        for(int i = 0; i < pars.length; i++){
            if(pars[i].equals(val)) return true;
        }
        return false;
    }
    
    /*参数错误*/
    public static final int STATE_PARAMETER_ERROR		=	-1;
    /*服务器发生错误*/
    public static final int STATE_SERVER_ERROR			=	-2;
    /*其他错误*/
    public static final int STATE_ERROR                 =   -3;
    /*未登陆*/
    public static final int STATE_NOT_LOGIN             =   -4;
    /*成功*/
    public static final int STATE_RESPONSE_SUCCESS		=	0;
    
    public String _createResponse(int state, String data){
    	if(data == null || data == "") data = "null";
    	String res = "";
    	switch (state) {
            case STATE_RESPONSE_SUCCESS:
                res = String.format("{\"state\":%d, \"message\":\"%s\", \"data\": %s}", state, "请求成功", data);
                break;
    		case STATE_PARAMETER_ERROR:
    			res = String.format("{\"state\":%d, \"message\":\"%s\", \"data\": %s}", state, "参数错误", data);
    			break;
    		case STATE_SERVER_ERROR:
    			res = String.format("{\"state\":%d, \"message\":\"%s\", \"data\": %s}", state, "服务器发生错误", data);
    			break;
    		case STATE_NOT_LOGIN:
    		    data = "请先登录";
            case STATE_ERROR:
                res = String.format("{\"state\":%d, \"message\":\"%s\", \"data\": %s}", state, data, "null");
                break;
		}
    	return res;
    }
    
    /**
     * 得到完整的url
     * @param request
     * @return
     */
    public String _getFullUrl(HttpServletRequest request){
        return request.getAttribute("fullurl") + "";
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
    
    public void _send(JSONObject json, HttpServletResponse response) throws IOException{
        _send(json.toString(), response);
    }
    
    public void _send(String str, HttpServletResponse response) throws IOException{
    	PrintWriter print = response.getWriter();
    	print.write(str);
    	print.flush();
    }
    
    public void tmp(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
    }
}
