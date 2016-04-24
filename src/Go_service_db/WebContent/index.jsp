<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	/*此处跳转至servlet*/
	request.getRequestDispatcher("db").forward(request, response);
%>
