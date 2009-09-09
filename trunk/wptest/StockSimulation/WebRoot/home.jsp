<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>finance analysis system</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="css/global.css">
		<style type="text/css">
#middle {
	width: 100%;
}

#mleft {
	width: 20%;
	float: left;
}

#mright {
	width: 79%;
	float: right;
}

root {
	display: block;
}

* {
	margin: 0;
	padding: 0;
}

div,code,p,h1,h2,address,dt,dd,li {
	font: normal 12px/ 1.5em Tahoma, "Lucida Grande", Helvetica, Verdana,
		Lucida, Arial, "Arial Unicode", sans-serif, serif
}

iframe {
	width: 1000px;
	height: 670px;
}
</style>
		<script type="text/javascript" src="js/jquery-1.3.1.js"></script>
		<script type="text/javascript">
			function forward(href){
				$('#mright').attr('src',href);
			}
			$(document).ready(
			 function(){
			 	$('#mright').click();
			 }
			);
		</script>
	</head>

	<body>
		<div id="top"></div>
		<div id="middle">
			<div id="mleft">
				<div>
					<a href="#" onclick="forward('chart/index.jsp');">股票行情</a>
				</div>
				<div>
					<a href="#">模拟炒股</a>
				</div>
			</div>
			<iframe id="mright" src="" frameborder="0"></iframe>
		</div>
		<div id="footer"></div>
	</body>
</html>
