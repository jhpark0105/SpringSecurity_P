<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String id = request.getParameter("id");
String password = request.getParameter("password");

//자격 증명 확인
String validId = "ok";
String validPwd = "111";

//인증(Authentification)
if(id != null && password != null && id.equals(validId) && password.equals(validPwd)){
	HttpSession httpSession = request.getSession();
	httpSession.setAttribute("userid", id);
	
	response.sendRedirect("success.jsp");
} else {
	//자격 증명이 유효하지 않은 경우 오류 메세지 출력
	out.println("<html><body>");
	out.println("<h3>login fail...</h3>");
	out.println("<a href='login.html'>retry login</a>");
	out.println("</body></html>");
}
%>