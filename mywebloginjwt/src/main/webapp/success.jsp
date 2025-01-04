<%@page import="io.jsonwebtoken.Jwts"%>
<%@page import="io.jsonwebtoken.Claims"%>
<%@page import="io.jsonwebtoken.Jws"%>
<%@page import="io.jsonwebtoken.security.Keys"%>
<%@page import="java.security.Key"%>
<%@page import="java.util.Base64"%>
<%@page import="io.jsonwebtoken.JwtException"%>
<%@page import="io.jsonwebtoken.ExpiredJwtException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//JWT로 확인
//쿠키에서 JWT 읽기
Cookie[] cookies = request.getCookies(); //클라이언트의 모든 쿠키를 가져옴
String jwt = null;

if(cookies != null){
	for(Cookie cookie : cookies){
		if(cookie.getName().equals("jwt")){
			jwt = cookie.getValue();
			break;
		}
	}
}

if(jwt != null){
	try {
	    // 서블릿 컨텍스트에서 Base64로 인코딩된 비밀 키 가져오기  java.util.Base64
	    String encodedKey = (String) getServletContext().getAttribute("secretKey");
	    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
	    Key secretKey = Keys.hmacShaKeyFor(decodedKey);
	    
		//JWT 유효성 검사
		Jws<Claims> claims = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(jwt); 
					//전달된 jwt를 파싱 후 유효하면 Jws<Claims>를 반환, 
					//아니면 JwtException(또는 ExpiredJwtException) 을 반환
		
		String userid = claims.getBody().getSubject(); //sub 클레임 읽기
		
		//유효한 경우 메세지 출력
%>
		<!DOCTYPE html>
		<html>
		<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		</head>
		<body>
			<h2>와우 인가(Authorization) 성공! (JWT 사용)</h2>
			<br>
			<%= userid %>님 쇼핑 페이지를 마음껏 즐기세요<p/>
			<a href="logout.jsp">로그아웃</a>
		</body>
		</html>
<%
	}catch(ExpiredJwtException e){
		System.out.println("만료된 토큰");
		response.sendRedirect("login.html");
	}catch(JwtException e){
		System.out.println("유효하지 않은 토큰");
		response.sendRedirect("login.html");
	}
} else {
	response.sendRedirect("login.html");
}
%>
