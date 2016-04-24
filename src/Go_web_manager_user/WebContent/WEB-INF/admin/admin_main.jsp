<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=path%>/css/admin.css">
<title>控制台</title>
</head>
<body>
<div class="top">top</div>
<div class="content">
    <div class="tags">
        <ul>
            <li class="state-active"><a href="javascript:void(0);" id="rudestate">概况</a></li>
            <li><a href="javascript:void(0);" id="appservicestate">应用服务器管理</a></li>
            <li><a href="javascript:void(0);" id="dbservicestate">数据库代理管理</a></li>
            <li><a href="javascript:void(0);" id="config">配置</a></li>
        </ul>
    </div>
    <div class="info">
        123
    </div>
</div>
<div class="footer">
    bootom
</div>
</body>
</html>