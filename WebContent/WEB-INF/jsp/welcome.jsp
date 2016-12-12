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
	<br>
	<div style="font-family: verdana; padding: 10px; border-radius: 10px; font-size: 12px; text-align:center;">
 		You are watching ${model.file}. Enjoy!
	</div>
	
	<video width="320" height="240" controls="controls">
  		<source src="vids/${model.id}" type="video/mp4" />
	</video>
	<br> <br>
	<i>This link is only valid for 24 hours. 
	Please refresh the page if you want to continue
	watching the video after the expiration time.</i>
</body>
</html>