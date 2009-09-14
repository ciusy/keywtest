
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="../js/jquery-1.3.1.js"></script>
<script type="text/javascript">
function reFreshChart(){
	$('#chart').attr('src','StoKlinechart.jsp'+'?stoCode='+$('#stoCode').attr('value'))+'&randowm='+Math.random();
}

</script>
<title>Stock chart</title>
</head>
<body>

<div id='chartFrom'>代码：<input type="text" id="stoCode" /> <input type="button" value="提交" onclick="reFreshChart();" /> </div>


<div><img src="StoKlinechart.jsp" alt="" id='chart' /></div>
</body>
</html>