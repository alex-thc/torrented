<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Item processed</title>
<link rel="icon" type="image/x-icon" href="resources/favicon.ico" />
<style type="text/css">
body {
	background-image: url('http://crunchify.com/bg.png');
}
</style>
</head>
<body>
	<br>
	<div style="text-align:center">
		<h2>
			<c:choose>
  				<c:when test="${empty error}">
  				Successfully added!<br> <br>
  				</c:when>
  				<c:otherwise>
  				Failed to add: ${error}<br> <br>
  				</c:otherwise>
			</c:choose>
			
		</h2>
		<h3>
			<a href="/WebAppTest/">Go back</a>
		</h3>
	</div>
</body>
</html>