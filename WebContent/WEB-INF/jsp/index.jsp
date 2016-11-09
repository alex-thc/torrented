<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<title>MongoDB Test Application</title>
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
			Hey You..!! This is my test MongoDB Java Spring MVC app with different integrations<br> <br>
		</h2>
	</div>
	
	<h3>
		Add a new item
	</h3>
	<form action="submit.html" method="POST">
      <input type="text" id="newItemUri" name="newItemUri" />
      <input type="submit" value="Submit" />
    </form>
	
	<h3>
		Active downloads
	</h3>
	<table>
    <tr>
        <th>Magnet</th>
        <th>Eta(min)</th>
        <th>%done</th>
        <th>Rate</th>
        <th>Status</th>
        <th>Finished</th>
    </tr>
    <c:forEach items="${activeItemsList}" var="item" varStatus="status">
        <tr>
            <td>${item.magnetLink}</td>
            <td><fmt:formatNumber type="number" maxFractionDigits="1" value="${item.eta/60}" /></td>
            <td><fmt:formatNumber type="number" maxFractionDigits="1" value="${item.percentDone*100}" /></td>
            <td>${item.rateDownload}</td>
            <td>${item.status}</td>
            <td>${item.finished}</td>
        </tr>
    </c:forEach>
    </table>
    
    <h3>
		Videos
	</h3>
	<table>
    <tr>
        <th>Name</th>
        <th>Added Date</th>
        <th>Status</th>
        <th>Files</th>
    </tr>
    <c:forEach items="${downloadedItemsList}" var="item" varStatus="status">
        <tr>
            <td>${item.name}</td>
            <td>${item.addedDate}</td>
            <td>
            	<c:choose>
            		<c:when test="${item.processing}">
        				Processing
    				</c:when>
    				<c:when test="${not empty item.filesToConvert}">
        				Needs conversion
    				</c:when>
    				<c:otherwise>
        				Ready
    				</c:otherwise>
				</c:choose>
            </td>
            <td><a href="welcome.html?file=${item.videoFiles[0]}">${item.videoFiles[0]}</a></td>
        </tr>
    </c:forEach>
	</table>
</body>
</html>