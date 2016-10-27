<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Spring MVC Tutorial Series by Crunchify.com</title>
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
			Hey You..!! This is your 1st Spring MCV Tutorial..<br> <br>
		</h2>
		<h3>
			<a href="welcome.html">Click here to See Welcome Message... </a>(to
			check Spring MVC Controller... @RequestMapping("/welcome"))
		</h3>
	</div>
	
	<form action="submit.html" method="POST">
      <input type="text" id="newItemUri" name="newItemUri" />
      <input type="submit" value="Submit" />
    </form>
	
	<table>
    <tr>
        <th>ID</th>
        <th>Status</th>
    </tr>
    <c:forEach items="${itemsList}" var="item" varStatus="status">
        <tr>
            <td>${item.id}</td>
            <td>${item.status}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>