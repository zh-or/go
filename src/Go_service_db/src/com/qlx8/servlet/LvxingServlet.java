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
import com.qlx8.dao.LvxingDao;
import com.qlx8.model.Lvxing;
import com.qlx8.model.Lx_Member;
import com.qlx8.model.User;
import com.qlx8.service.MainService;
import com.qlx8.util.BaseServlet;
import com.qlx8.util.C;
import com.qlx8.util.T;

@WebServlet(name = "LvxingServlet", urlPatterns = "/lvxing")
public class LvxingServlet extends BaseServlet{
    private static final long serialVersionUID = 1L;

    private MainService s;
    private Cache cache;
    private Log log;
    
    
    
    public LvxingServlet() {
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
        /*这里所有的调用都是需要登录的. */
       /* if(_filterStrings(new String[]{"reg", "login"}, method)){//需要忽略验证的函数
            return true;
        }*/
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
    
    /*创建一个旅行*/
    public void createlvxing(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"token", "explain", "name", "begin_lng", "begin_lat", "begin_addr", "end_addr", "begin_time", "end_time"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        /* 旅行创建时记录创建的位置的经纬度?
         * 结束旅行时记录经纬度作为结束点
         * 创建时则直接选地址即可
         * */
        String token = request.getParameter("token");
        String name = request.getParameter("name");
        String explain = request.getParameter("explain");
        double begin_lng = Util.StrtoDouble(request.getParameter("begin_lng"));
        double begin_lat = Util.StrtoDouble(request.getParameter("begin_lat"));
        int begin_addr = Util.StrtoInt(request.getParameter("begin_addr"));
        int end_addr = Util.StrtoInt(request.getParameter("end_addr"));
        long begin_time = Util.StrtoLong(request.getParameter("begin_time"));
        long end_time = Util.StrtoLong(request.getParameter("end_time"));
        User u = cache.get(token);
        String str_res = "";
        LvxingDao ldao = null;
        try {
            ldao = new LvxingDao();
            Lvxing lx = new Lvxing(0,
                    0,
                    u.id,
                    s.createLxNumber(),
                    name,
                    explain,
                    begin_lng,
                    begin_lat,
                    0,
                    0d,
                    begin_addr,
                    end_addr,
                    begin_time,
                    end_time,
                    System.currentTimeMillis(),
                    false);
            boolean isok = ldao.add(lx);
            if(isok){
                str_res = _createResponse(STATE_RESPONSE_SUCCESS, String.format("{\"id\": %d}", lx.id));
            }else{
                str_res = _createResponse(STATE_ERROR, "创建旅行失败");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(ldao != null) ldao.close();
        }
        _send(str_res, response);
    }
    
    /*修改旅行信息*/
    public void updatelvxing(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"name", "value", "lvxingid"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        /*
         * name = name, explain
         * */
        String token = request.getParameter("token");
        int lxid = Util.StrtoInt(request.getParameter("lvxingid"));
        String name = request.getParameter("name");
        String value = request.getParameter("value");
        String str_res = "";
        if(_filterStrings(new String[]{"name", "explain"}, name)){
            LvxingDao ldao = null;
            try {
                ldao = new LvxingDao();
                boolean isok = ldao.updateinfo(lxid, name, value);
                if(isok){
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, "");
                }else{
                    str_res = _createResponse(STATE_ERROR, "修改信息失败");
                }
            } catch (Exception e) {
                log.error(Format.formatException(e));
                s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
                str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
            }finally{
                if(ldao != null) ldao.close();
            }
        }else{
            s.commentRunningLog(
                    C.SUBSCRIBETYPE.ExceptionLog,
                    "有人违规修改个人信息:" +  String.format("name:%s, value:%s, token:%s, ip:%s, ", name, value, token, T.getIpAddress(request)));
            str_res = _createResponse(STATE_ERROR, "请不要违规操作");
        }
        _send(str_res, response);
    }
    
    /*多功能查询*/
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        if(!_checkparameter(new String[]{"type", "where"}, request) && !_filterStrings(new String[]{"owner", "number", "begin_addr", "end_addr", "lvxing"}, type)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        /*
         * 多种查询方式
         * */
        String token = request.getParameter("token");
        String where = request.getParameter("where");
        int page = Util.StrtoInt(request.getParameter("page"));
        int pagetotal = Util.StrtoInt(request.getParameter("pagetotal"));
        
        if(page <= 0){
            page = 1;
        }
        if(pagetotal <= 0){
            pagetotal = 10;
        }
        User u = cache.get(token);
        String str_res = "";
        LvxingDao ldao = null;
        try {
            ldao = new LvxingDao();
            switch (type) {
                case "number":/*旅行号码*/
                {
                    Lvxing lx = ldao.queryByNumber(Util.StrtoInt(where));
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, lx != null ? lx.toJSON() : "");
                }
                    break;
                case "lvxing":/*旅行id*/
                {
                    Lvxing lx = ldao.queryByLvxingId(Util.StrtoInt(where));
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, lx != null ? lx.toJSON() : "");
                }
                    break;
                case "owner":/*自己所创建的旅行*/
                {
                    ArrayList<Lvxing> lxs = ldao.queryByPage(u.id, page, pagetotal);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for(Lvxing lx : lxs){
                        sb.append(lx.toJSON());
                        sb.append(",");
                    }
                    if(lxs.size() > 0){
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]");
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, sb.toString());
                }
                    break;
                case "begin_addr":/*开始地址*/
                {
                    ArrayList<Lvxing> lxs = ldao.queryByBeginAddr(Util.StrtoInt(where), page, pagetotal);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for(Lvxing lx : lxs){
                        sb.append(lx.toJSON());
                        sb.append(",");
                    }
                    if(lxs.size() > 0){
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]");
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, sb.toString());
                }
                    break;
                case "end_addr":/*结束地址*/
                {
                    ArrayList<Lvxing> lxs = ldao.queryByEndAddr(Util.StrtoInt(where), page, pagetotal);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for(Lvxing lx : lxs){
                        sb.append(lx.toJSON());
                        sb.append(",");
                    }
                    if(lxs.size() > 0){
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]");
                    str_res = _createResponse(STATE_RESPONSE_SUCCESS, sb.toString());
                }
                    break;
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(ldao != null) ldao.close();
        }
        _send(str_res, response);
    }
    
    /*获取旅行成员列表*/
    public void memberlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"lvxingid"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        int lxid = Util.StrtoInt(request.getParameter("lvxingid"));
        int page = Util.StrtoInt(request.getParameter("page"));
        int pagetotal = Util.StrtoInt(request.getParameter("pagetotal"));
        
        if(page <= 0){
            page = 1;
        }
        if(pagetotal <= 0){
            pagetotal = 10;
        }
        String str_res = "";
        LvxingDao ldao = null;
        try {
            ldao = new LvxingDao();
            ArrayList<Lx_Member> ms = ldao.queryLxMember(lxid, page, pagetotal);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(Lx_Member lx : ms){
                sb.append(lx.toJSON());
                sb.append(",");
            }
            if(ms.size() > 0){
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            str_res = _createResponse(STATE_RESPONSE_SUCCESS, sb.toString());
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(ldao != null) ldao.close();
        }
        _send(str_res, response);
    }
    
    /*请求加入旅行*/
    public void reqjoinlvxing(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"lvxingid"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String str_res = "";
        /*推送一条消息就行了?*/
        int id = Util.StrtoInt(request.getParameter("lvxingid"));
        LvxingDao ldao = null;
        try {
            ldao = new LvxingDao();
            Lvxing lx = ldao.queryByLvxingId(id);
            if(lx != null){
                
            }else{
                str_res = _createResponse(STATE_ERROR, "你要加入的旅行并不存在");
            }
        } catch (Exception e) {
            log.error(Format.formatException(e));
            s.commentRunningLog(C.SUBSCRIBETYPE.ExceptionLog, Format.formatException(e));
            str_res = _createResponse(STATE_SERVER_ERROR, "服务器发生错误");
        }finally{
            if(ldao != null) ldao.close();
        }
        _send(str_res, response);
    }
}
