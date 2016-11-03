<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Alex&Yulia's video corner</title>
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
			Hey You..!! We proudly present this nice tool for your entertainment!<br> <br>
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
        <th>Status</th>
        <th>Finished</th>
    </tr>
    <c:forEach items="${activeItemsList}" var="item" varStatus="status">
        <tr>
            <td>${item.magnetLink}</td>
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