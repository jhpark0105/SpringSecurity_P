<%@page import="java.util.Base64"%>
<%@page import="java.util.Date"%>
<%@page import="io.jsonwebtoken.SignatureAlgorithm"%>
<%@page import="io.jsonwebtoken.Jwts"%>
<%@page import="java.security.Key"%>
<%@page import="io.jsonwebtoken.security.Keys"%>
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
	//JWT를 생성 후 클라이언트의 쿠키에 저장
	// 고정된 비밀 키 사용 (예제용)  최소 256비트 길이의 비밀 키
    //String secretKeyString = "mySuperSecretKey12345678901234567890123456789012"; 
    //Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    
    // 서블릿 컨텍스트에서 Base64로 인코딩된 비밀 키 가져오기  java.util.Base64
    String encodedKey = (String) getServletContext().getAttribute("secretKey");
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    Key secretKey = Keys.hmacShaKeyFor(decodedKey);
	
 	// 토큰 생성
    String jwt = Jwts.builder()
                    .setSubject(id)         // 토큰 용도(제목)
                    .setIssuedAt(new Date())      // 생성 시간 설정
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))   // 토큰 만료 시간 설정. 1시간 유효
                    .signWith(SignatureAlgorithm.HS256, secretKey)   // HS256과 Key로 Sign
                    .compact();  // 토큰 생성
                    
   // 쿠키에 JWT 저장
   Cookie jwtCookie = new Cookie("jwt", jwt);
   jwtCookie.setHttpOnly(true); //수정 불가능, XSS 공격 방지 목적 
   jwtCookie.setPath("/");   //모든 경로에서 쿠키를 허용하게 함
   //jwtCookie.setDomain("aa.com");
   response.addCookie(jwtCookie);
	
	response.sendRedirect("success.jsp");
} else {
	//자격 증명이 유효하지 않은 경우 오류 메세지 출력
	out.println("<html><body>");
	out.println("<h3>login fail...</h3>");
	out.println("<a href='login.html'>retry login</a>");
	out.println("</body></html>");
}
%>