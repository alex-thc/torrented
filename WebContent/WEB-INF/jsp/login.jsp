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
		<form:form id="loginForm" method="post" action="login" modelAttribute="loginInfo">

			<form:label path="username">Enter your user-name</form:label>
			<form:input id="username" name="username" path="username" /><br>
			<form:label path="username">Please enter your password</form:label>
			<form:password id="password" name="password" path="password" /><br>
			<input type="submit" value="Submit" />
		</form:form>
		<form action="register" method="GET">
      		<input type="submit" value="New user?" />
    	</form>
	</body>
</html>