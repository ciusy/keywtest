<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.mrj.util.chart.*,org.jfree.chart.*,java.io.*,java.util.*,java.text.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<%
SimpleDateFormat  sdf = new SimpleDateFormat("MM/dd/yy");

String stoCode=request.getParameter("stoCode");
String from=request.getParameter("fromDate");
String to=request.getParameter("toDate");

Date fromdate=new Date();
if(from!=null){
	fromdate=sdf.parse(from);//fromMMDDYYYY
}else{
	Calendar fromCalendar =Calendar.getInstance();
	fromCalendar.setTime(fromdate);
	fromCalendar.add(Calendar.DAY_OF_YEAR,-90);
	fromdate=fromCalendar.getTime();
}

Date todate=new Date();
if(to!=null){
	todate=sdf.parse(to);//fromMMDDYYYY
}else{
	Calendar toCalendar =Calendar.getInstance();
	toCalendar.setTime(todate);
	toCalendar.add(Calendar.DAY_OF_YEAR,-90);
	todate=toCalendar.getTime();
}



OutputStream out1 = response.getOutputStream();
	JFreeChart chart = StoKline.getChart(stoCode,fromdate,todate);
	response.setContentType("image/png");
	ChartUtilities.writeChartAsPNG(out1, chart, 400, 300);
%>


</body>
</html>