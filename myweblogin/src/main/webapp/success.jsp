<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//세션으로 확인
HttpSession httpSession = request.getSession(false);
if(httpSession != null && httpSession.getAttribute("userid") != null){
	String userid = (String)httpSession.getAttribute("userid");
%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	</head>
	<body>
		<h2>와우 인가(Authorization) 성공!</h2>
		<br>
		쇼핑 페이지를 즐기세요<p/>
		<a href="logout.jsp">로그아웃</a>
	</body>
	</html>
<%
} else {
	response.sendRedirect("login.html");
}
%>
