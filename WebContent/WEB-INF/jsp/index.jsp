<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<title>Torrent Video Streaming</title>
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
			Welcome to my torrent video streaming service<br> <br>
			This service is for demonstration purposes only and all
			copyright content will be eventually cleaned up.<br> <br>
		</h2>
	</div>
	
	<h3>
		Add a new item (use a link to your torrent file or a magnet link)
	</h3>
		<i>Use torrents with *.mp4 videos for best results. *.avi and *.mkv 
		are also supported, but they need to be converted, and it can
		take up to a day.</i>
	<form action="submit.html" method="POST">
      <input type="text" id="newItemUri" name="newItemUri" />
      <input type="submit" value="Submit" />
    </form>
	
	<h3>
		Active downloads
	</h3>
	<table>
    <tr>
        <th>Name</th>
        <th>Eta(min)</th>
        <th>%done</th>
        <th>Rate</th>
        <th>Status</th>
        <th>Finished</th>
    </tr>
    <c:forEach items="${activeItemsList}" var="item" varStatus="status">
        <tr>
            <td>${item.name}</td>
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
            <td>
            <c:forEach items="${item.videoFiles}" var="video" varStatus="status">
            	<a href="welcome.html?file=${video}">${video}</a><br/>
            </c:forEach>
            </td>
        </tr>
    </c:forEach>
	</table>
</body>
</html>