package com.qlx8.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.cluster.Message;
import lotus.cluster.MessageFactory;
import lotus.cluster.node.NodeSession;
import lotus.format.Format;
import lotus.log.Log;
import lotus.util.Util;

import com.qlx8.model.SmsCode;
import com.qlx8.service.MainService;
import com.qlx8.util.BaseServlet;
import com.qlx8.util.C;

@WebServlet(name = "ToolServlet", urlPatterns = "/tool")
public class ToolServlet extends BaseServlet{
    private static final long serialVersionUID = 1L;
    private MainService s;
    private Log         log;
    private MessageFactory mf;
    
    public ToolServlet() {
        s = MainService.getInstanse();
        mf = MessageFactory.getInstance();
        log = s.getLog();
    }

    @Override
    public void _do(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _send("不知道你在干什么", response);
    }

    @Override
    public void _exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.log(Format.formatException(e));
        sendexception(e);
        try {
            _send(_createResponse(STATE_ERROR, ""), response);
        } catch (IOException e1) {}
    }
    
    /**
     * 把异常发送到管理端去
     * @param e
     */
    private void sendexception(Exception e){
        String str_e = Format.formatException(e);
        log.log(str_e);
        NodeSession session = s.getMessageQueueSession();
        if(session != null){
            Message msg = mf.create(false, Message.MTYPE_SUBSCRIBE, C.SUBSCRIBETYPE.ExceptionLog, s.get, , "UserServletException", str_e.getBytes())
            
        }
    }
   
    public void sendsmscode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!_checkparameter(new String[]{"phone"}, request)){
            _send(_createResponse(STATE_PARAMETER_ERROR, ""), response);
            return;
        }
        String phone = request.getParameter("phone");
        SmsCode smscode = s.getSmsCode(phone);
        if(smscode != null){
            _send(_createResponse(STATE_ERROR, "已经发送验证码"), response);
            return;
        }
        String code = "";
        code += Util.RandomNum(0, 9) + "";
        code += Util.RandomNum(0, 9) + "";
        code += Util.RandomNum(0, 9) + "";
        code += Util.RandomNum(0, 9) + "";
        
        log.info("phone: %s, code: %s", phone, code);
        s.pushSmsCode(phone, code);
        s.commentRunningLog(C.SUBSCRIBETYPE.REG_SMS_CODE, String.format("time:%s, phone:%s, code:%s", Format.formatTime(System.currentTimeMillis()), phone, code));
        _send(_createResponse(STATE_RESPONSE_SUCCESS, ""), response);
    }
}
