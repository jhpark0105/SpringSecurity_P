<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//클라이언트 쿠키에 남아있는 jwt 삭제(선택적)
Cookie cookie = new Cookie("jwt", null);
cookie.setMaxAge(0); //유효시간 만료
//cookie.setPath(request.getContextPath()); 
cookie.setPath("/"); 
response.addCookie(cookie);

response.sendRedirect("login.html");
%>