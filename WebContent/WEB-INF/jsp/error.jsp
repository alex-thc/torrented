<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Error</title>
		<link rel="icon" type="image/x-icon" href="resources/favicon.ico" />
	</head>
	<body>
		<div style="text-align:center">
			<h2>
				Something went wrong<br> <br>
			</h2>
		</div>
		<c:choose>
  			<c:when test="${not empty message}">
  				Error: ${message}
  			</c:when>
		</c:choose>
	</body>
</html>