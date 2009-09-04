
<%@ page language="java" contentType="image/png; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="com.mrj.util.chart.*,org.jfree.chart.*,java.io.*,java.util.*,java.text.*;"%>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

	String stoCode = request.getParameter("stoCode");
	String from = request.getParameter("fromDate");
	String to = request.getParameter("toDate");

	if (stoCode == null)
		stoCode = "000725";	

	Date todate = sdf.parse(sdf.format(new Date()));
	if (to != null) {
		todate = sdf.parse(to);//fromMMDDYYYY
	} else {
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(todate);
		toCalendar.add(Calendar.DAY_OF_YEAR, -30);
		todate = toCalendar.getTime();
	}
	
	
	Date fromdate = sdf.parse(sdf.format(new Date()));
	if (from != null) {
		fromdate = sdf.parse(from);//fromMMDDYYYY
	} else {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(todate);
		fromCalendar.add(Calendar.DAY_OF_YEAR, -90);
		fromdate = fromCalendar.getTime();
	}

	response.flushBuffer();

	OutputStream out1 = response.getOutputStream();
	JFreeChart chart = StoKline.getChart(stoCode, fromdate, todate);//
	response.setContentType("image/png");
	ChartUtilities.writeChartAsPNG(out1, chart, 800, 600);

	response.flushBuffer();
	out.clear();
	out = pageContext.pushBody();
%>
