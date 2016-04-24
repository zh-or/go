package com.qlx8.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.util.Util;

import com.qlx8.dao.UserDao;
import com.qlx8.model.User;
import com.qlx8.util.BaseServlet;

/**
 * @author yangfan
 */
@WebServlet(name = "_UserServlet", urlPatterns = "/_user")
public class _UserServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void _do(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _send(_createResponse(STATE_ERROR, "不知道你在干什么"), response);
    }

    @Override
    public boolean _check(String method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*处理缓存相关的 key => url, value => json*/
        if("GET".equals(request.getMethod())){
            String url = _getFullUrl(request);
            System.out.println("url:" + url);
        }
        return true;
    }
    
    @Override
    public void _exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        try {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        } catch (IOException e1) {/*这个就不管了*/}
    }

    /**
     * 查询用户信息 (账号&密码)
     * @param request
     * @param response
     * @throws IOException
     */
    public void queryUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!_checkparameter(new String[] { "name", "password" }, request)) {
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        UserDao udao = null;
        try {
            udao = new UserDao();
            User user = udao.query(request.getParameter("name"), request.getParameter("password"));
            _send(_createResponse(STATE_RESPONSE_SUCCESS, user.toJSON()), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        } finally {
            if (udao != null) udao.close();
        }
    }

    /**
     * 获取用户列表 (分页)
     * @param request
     * @param response
     * @throws Exception
     */
    public void queryUserByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int page = 1, total = 10;
        if (!_checkparameter(new String[] { "page", "total" }, request)) {
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        page = Util.StrtoInt(request.getParameter("page"));
        total = Util.StrtoInt(request.getParameter("total"));
        if(total <= 0) total = 10;
        UserDao udao = null;
        try {
            udao = new UserDao();
            ArrayList<User> users = udao.queryByPage(page, total);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int i = 0; i < users.size(); i++){
                sb.append(users.get(i).toJSON());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            _send(_createResponse(STATE_RESPONSE_SUCCESS, sb.toString()), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        } finally {
            if (udao != null) udao.close();
        }
    }
    
    /**
     * 取用户总页数 (每页显示用户数)
     * @param request
     * @param response
     * @throws Exception
     */
    public void queryUserMaxPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int total = 0;
        if (!_checkparameter(new String[] {"total"}, request) || (total = Util.StrtoInt(request.getParameter("total"))) == 0) {
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        UserDao udao = null;
        int totalpage = 0;
        try {
            udao = new UserDao();
            totalpage = udao.userTotal();
            if(totalpage % total > 0){
                totalpage = totalpage / total + 1;
            }else{
                totalpage = totalpage / total;
            }
            _send(_createResponse(STATE_RESPONSE_SUCCESS, String.format("{\"totalpage\": %d}", totalpage)), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        } finally {
            if (udao != null) udao.close();
        }
    }
    
    /**
     * 设置用户是否启用
     * @param request
     * @param response
     * @throws Exception
     */
    public void setUserEnable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"uid", "enable"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        UserDao udao = null;
        try {
            udao = new UserDao();
            boolean isok = udao.setEnable(Util.StrtoInt(request.getParameter("uid")), "1".equals(request.getParameter("enable")));
            _send(_createResponse(STATE_RESPONSE_SUCCESS, String.format("{\"isok\":%s}", isok)), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        }finally{
            if(udao != null) udao.close();
        }
    }
    
    /**
     * 添加用户
     * @param request
     * @param response
     * @throws Exception
     *//*
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        这些参数可以为空后期再修改之
        if(!_checkparameter(new String[]{"address", "phone", "password", "nickname", "sex", "headpic", "birthday", "lat", "lng"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        UserDao udao = null;
        try {
            udao = new UserDao();
            boolean isok = udao.add(new User(  0,
                                                Util.StrtoInt(request.getParameter("address")),
                                                request.getParameter("phone"),
                                                request.getParameter("password"),
                                                request.getParameter("nickname"),
                                                Util.strHash(request.getParameter("nickname")),
                                                Util.StrtoInt(request.getParameter("sex")),
                                                request.getParameter("headpic"),
                                                Util.str
                                                Util.StrtoLong(request.getParameter("birthday")),
                                                System.currentTimeMillis(),
                                                System.currentTimeMillis(),
                                                true));
            _send(_createResponse(STATE_RESPONSE_SUCCESS, String.format("{\"isok\":%s}", isok)), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        }finally{
            if(udao != null) udao.close();
        }
    }*/
    
    /**
     * 修改单个信息
     * @param request
     * @param response
     * @throws Exception
     */
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"name", "value", "uid"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        UserDao udao = null;
        try {
            udao = new UserDao();
            boolean isok = udao.updateInfo(Util.StrtoInt(request.getParameter("uid")), 
                                            request.getParameter("name"),
                                            request.getParameter("value"));
            _send(_createResponse(STATE_RESPONSE_SUCCESS, String.format("{\"isok\":%s}", isok)), response);
        } catch (Exception e) {
            _send(_createResponse(STATE_SERVER_ERROR, ""), response);
        }finally{
            if(udao != null) udao.close();
        }
    }
    
    
}
