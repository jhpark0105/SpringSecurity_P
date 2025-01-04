<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
HttpSession httpSession = request.getSession(false);
httpSession.removeAttribute("userid"); //서버에 저장된 session을 삭제

//클라이언트 쿠키에 남아있는 JSESSIONID 삭제(선택적)
Cookie cookie = new Cookie("JSESSIONID", null);
cookie.setMaxAge(0); //유효시간 만료
cookie.setPath(request.getContextPath()); 
response.addCookie(cookie);

response.sendRedirect("login.html");
%>