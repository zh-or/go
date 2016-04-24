package com.qlx8.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.qlx8.service.MainService;
import com.qlx8.util.C;

/**
 * @author OR
 */
@WebFilter(urlPatterns="/*")
public class UrlFilter implements javax.servlet.Filter{
    private String encode = "UTF-8";
    private String contenttype = "text/html;charset=utf-8";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding(encode);
        response.setCharacterEncoding(encode);
        response.setContentType(contenttype);//加个头防止乱码
        HttpServletRequest req = (HttpServletRequest) request;
        /*这里先把完整的url搞出来, 免得后面的servlet取不出来*/
        StringBuffer fullurl = req.getRequestURL();
        String tmp = req.getQueryString();
        if(tmp != null) fullurl.append("?").append(tmp);
        request.setAttribute("fullurl", fullurl.toString());
        
        String path = req.getServletPath();
    //    System.out.println("请求路径:" + path);
        
        String newpath = "";
        int begin = path.lastIndexOf("/");

        if(path.length() > 3 && ".do".equals(path.substring(path.length() - 3))){//最后3个字符是否是.do
            newpath = path.substring(0, begin);
            newpath += "?m=" + path.substring(begin + 1, path.lastIndexOf("."));
            begin = path.lastIndexOf("?");
            if(begin > 0){//+上get参数
                newpath += "&" + path.substring(begin);
            }
            req.getRequestDispatcher(newpath).forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        encode = MainService.getInstanse().getConfig().getStringValue(C.INI.MAIN, C.INI.ENCODEING, "UTF-8");
        contenttype = "text/html;charset=" + encode;
    }
    
    @Override
    public void destroy() {}

}
