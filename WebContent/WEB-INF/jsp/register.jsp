<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<link rel="icon" type="image/x-icon" href="resources/favicon.ico" />
	</head>
	<body>
		<c:choose>
  			<c:when test="${not empty message}">
  				Error: ${message}
  			</c:when>
		</c:choose>
		<form:form id="loginForm" method="post" action="register" modelAttribute="loginInfo">

			<form:label path="username">Enter your user-name</form:label>
			<form:input id="username" name="username" path="username" /><br>
			<form:label path="username">Please enter your password</form:label>
			<form:password id="password" name="password" path="password" /><br>
			<form:label path="inviteCode">Please enter the invite code (if you have one)</form:label>
			<form:password id="inviteCode" name="invite code" path="inviteCode" /><br>
			<input type="submit" value="Register" />
			
			<br><br><br>
			By clicking "Register" you agree not to use this service
			for anything substantially illegal, to treat kittens with
			respect and to mischievously smile to strangers for 5 days.
			
			<br><br><br>
			Limitations: 4 new items and 10 file downloads per day. Unlimited vids, yaay! 
			
		</form:form>
	</body>
</html>