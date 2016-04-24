package com.qlx8.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.format.Format;
import lotus.log.Log;
import lotus.util.Util;

import com.qlx8.cache.Cache;
import com.qlx8.dao.FriendsDao;
import com.qlx8.dao.LoginLogDao;
import com.qlx8.dao.UserDao;
import com.qlx8.model.Friend;
import com.qlx8.model.LoginLog;
import com.qlx8.model.SmsCode;
import com.qlx8.model.User;
import com.qlx8.service.MainService;
import com.qlx8.util.BaseServlet;
import com.qlx8.util.C;
import com.qlx8.util.T;
/**
 * @author yangfan
 */
@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet{

    private static final long serialVersionUID = 1L;
    
    private MainService s;
    private Cache cache;
    private Log log;

    public UserServlet() {
        s = MainService.getInstanse();
        cache = Cache.getInstance();
        log = s.getLog();
    }

    @Override
    public void _do(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _send("不知道你在干什么 / what are u doing?", response);
    }

    @Override
    public void _exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String str_e = Format.formatException(e);
        log.log(str_e);
        s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, "致命异常:" + str_e);
        try {
            _send(_createResponse(STATE_ERROR, ""), response);
        } catch (IOException e1) {}
    }
    
    @Override
    public boolean _check(String method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(_filterStrings(new String[]{"reg", "login"}, method)){
            return true;
        }
        String token = request.getParameter("token");
        if(Util.CheckNull(token)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return false;
        }
        if(null == cache.get(token)){
            _send(_createResponse(STATE_NOT_LOGIN, ""), response);
            return false;
        }
        return true;
        /*返回 true 时则调用后面的函数, 否则则直接返回*/
    }
    
    /**
     * 注册
     */
    public void reg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"name", "password", "phone", "lng", "lat", "smscode"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String lng = request.getParameter("lng");
        String lat = request.getParameter("lat");
        String smscode = request.getParameter("smscode");
        
        SmsCode scode = s.getSmsCode(phone);
        String res_str = "";
        if(scode != null && smscode.equals(scode.codevalue)){/*短信验证码正确*/
            s.removeSmsCode(scode.phone);
            UserDao udao = null;
            try {
                udao = new UserDao();
                User user = new User(   0,
                                        0,
                                        phone, 
                                        Util.strHash(phone),
                                        Util.EnCode(password + C.SYS.SALT, Util.EN_TYPE_MD5),
                                        name, Util.strHash(name),
                                        C.SYS.SEX_FEMALE,
                                        C.SYS.HEAD_DEF,
                                        0,
                                        Util.StrtoDouble(lat), 
                                        Util.StrtoDouble(lng),
                                        System.currentTimeMillis(),
                                        System.currentTimeMillis(),
                                        true);
                boolean isok = udao.add(user);
                if(isok){
                    String token = Util.getUUID();
                    user.setAttr(User.KEY_TOKEN, token);
                    res_str = _createResponse(STATE_RESPONSE_SUCCESS, "{\"token\": \"" + user.getAttr(User.KEY_TOKEN) + "\"}");
                    cache.put(token, user);
                }else{
                    log.log(Log.L_E, "注册账号出现错误");
                    res_str = _createResponse(STATE_ERROR, "注册失败, 请重试.");
                }
            } catch (Exception e) {
                log.error(Format.formatException(e));
                s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
                res_str = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
            }finally{
                if(udao != null) udao.close();
            }
        }else{
            res_str = _createResponse(STATE_ERROR, "短信验证码过期");
        }
        _send(res_str, response);
    }
    
    /**
     * 登陆
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(!_checkparameter(new String[]{"phone", "password", "lng", "lat", "device_type"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        
        String phone = request.getParameter("phone");

        String password = request.getParameter("password");
        String lng = request.getParameter("lng");
        String lat = request.getParameter("lat");
        String device_type = request.getParameter("device_type");
        String ip = T.getIpAddress(request);
        
        String str_res = "";
        User user = null;
        UserDao udao = null;
        try {
            udao = new UserDao();
            user = udao.query(phone, Util.EnCode(password + C.SYS.SALT, Util.EN_TYPE_MD5));
            if(user != null){
                if(user.isenable){
                    String token = Util.getUUID();/*创建一个新的token*/
                    String lastt = cache.check(phone);
                    /*此处还需要判断设备类型什么的, 现在先不写了. fuck.~!!!!*/
                    if(!Util.CheckNull(lastt)){/*检查是否已经登录过了?*/
                        token = lastt;
                    }
                    user.setAttr(User.KEY_TOKEN, token);
                    cache.put(token, user);
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, "{\"token\":\"" + token + "\"}");
                    LoginLogDao lldao = LoginLogDao.getInstance();
                    lldao.pushLog(new LoginLog(0,
                                                user.id,
                                                System.currentTimeMillis(),
                                                Util.StrtoDouble(lat),
                                                Util.StrtoDouble(lng),
                                                "未实现 :(",
                                                ip,
                                                Util.ip2int(ip),
                                                device_type));
                }else{
                    str_res = _createResponse(STATE_ERROR, "你的账号已被禁用");
                }
            }else{
                str_res = _createResponse(STATE_ERROR, "账号或密码错误");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_ERROR, "服务器发生错误");
        }finally{
            if(udao != null) udao.close();
        }
        _send(str_res, response);
    }
    
    /**
     * 修改用户单个信息
     */
    public void updateuserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"name", "value", "token"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String name = request.getParameter("name");
        String token = request.getParameter("token");
        String value = request.getParameter("value");
        User u =  cache.get(token);
        String str_res = "";
        UserDao udao = null;
        try {
            if(_filterStrings(new String[]{"token", "id", "isenable"}, name)){
                s.commentRunningLog(
                        C.SUBSCRIBETYPE.ExceptionLog,
                        "有人违规修改个人信息:" +  String.format("name:%s, value:%s, token:%s, ip:%s, ", name, value, token, T.getIpAddress(request)));
                str_res = _createResponse(STATE_ERROR, "请不要违规操作");
            }else{
                udao = new UserDao();
                boolean isok = udao.updateInfo(u.id, 
                                                name,
                                                value);
                
                if(isok){
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, null);
                }else{
                    str_res = _createResponse(STATE_ERROR, "未知原因失败");
                }
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(udao != null) udao.close();
        }
        _send(str_res, response);
    }
    
    public void queryuser(HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(!_checkparameter(new String[]{"account", "token"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String account = request.getParameter("account");//可传电话号码|昵称
        account = account.replaceAll(" ", "");
        String str_res = "";
        UserDao udao = null;
        try {
            udao = new UserDao();
            User user = null;
            if(T.isNumber(account)){/*如果都是数字则用电话好吗查询， 否则使用昵称查询*/
                user = udao.queryUserByPhone(account);
            }else{
                user = udao.queryUserByNickname(account);
            }
            if(user != null){
                str_res = _createResponse(STATE_RESPONSE_SUCCESS, user.toJSON_q());
            }else{
                str_res = _createResponse(STATE_ERROR, "你所查找的用户不存在");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }
        _send(str_res, response);
    }
    
    public void addfriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"uid", "remark", "token"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String token = request.getParameter("token");
        int uid = Util.StrtoInt(request.getParameter("uid"));
        String remark = request.getParameter("remark");
        
        User u =  cache.get(token);
        String str_res = "";
        FriendsDao fdao = null;
        UserDao udao = null;
        try {
            udao = new UserDao();
            User ufriend = udao.query(uid);
            if(ufriend != null){
                fdao = new FriendsDao();
                boolean isok = fdao.add(u.id, uid, remark);
                if(isok){
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, ufriend.toJSON_q());
                }else{
                    str_res = _createResponse(STATE_ERROR, "添加好友失败.");
                }
            }else{
                str_res = _createResponse(STATE_ERROR, "添加好友失败, 你要添加的人被外星人吃了.");
            } 
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(fdao != null) fdao.close();
            if(udao != null) udao.close();
        }
        _send(str_res, response);
    }
    
    public void delfriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"id", "token"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        int id = Util.StrtoInt(request.getParameter("id"));
        String str_res = "";
        FriendsDao fdao = null;
        try {
            fdao = new FriendsDao();
            boolean isok = fdao.deleteFriend(id);
            if(isok){
                str_res = _createResponse(STATE_RESPONSE_SUCCESS, "");
            }else{
                str_res = _createResponse(STATE_ERROR, "删除好友失败.");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(fdao != null) fdao.close();
        }
        _send(str_res, response);
    }
    
    public void friendslist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getParameter("token");
        
        User u =  cache.get(token);
        String str_res = "";
        FriendsDao fdao = null;
        try {
            fdao = new FriendsDao();
            ArrayList<Friend> fs = fdao.queryAllFriends(u.id);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(Friend f : fs){
                sb.append(f.toJSON());
                sb.append(",");
            }
            if(fs.size() > 0) sb.deleteCharAt(sb.length() - 1);/*删除最后一个逗号*/
            sb.append("]");
            str_res = _createResponse(STATE_RESPONSE_SUCCESS, sb.toString());
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(fdao != null) fdao.close();
        }
        _send(str_res, response);
    }
    
    public void updatefriendremark(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"id", "remark"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        int id = Util.StrtoInt(request.getParameter("id"));
        String remark = request.getParameter("remark");
        String str_res = "";
        FriendsDao fdao = null;
        try {
            fdao = new FriendsDao();
            boolean isok = fdao.updateRemark(id, remark);
            if(isok){
                str_res = _createResponse(STATE_RESPONSE_SUCCESS, "");
            }else{
                str_res = _createResponse(STATE_ERROR, "修改备注失败.");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(fdao != null) fdao.close();
        }
        _send(str_res, response);
    }
    
}
