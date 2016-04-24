<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=path%>/css/admin.css">

<title>登陆</title>
</head>
<body>
<div class="ct-h" style="margin-top: 10%;">
	<h1>登陆后台</h1>
	<form action="" method="post">
		请输入密码：<input type="password" name="pwd"> <input type="submit" value="登陆">
	</form>
</div>
</body>
</html>